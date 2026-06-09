package com.example.product_service.service.impl;

import com.example.product_service.dto.request.CreateProductReq;
import com.example.product_service.dto.request.ProductFilterReq;
import com.example.product_service.dto.request.ProductLockReq;
import com.example.product_service.dto.request.UpdateProductReq;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.ApplicationException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class  ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository ;
    private final CategoryRepository categoryRepository ;
    private final ProductMapper productMapper ;
    private final RedissonClient redissonClient ;


    @Override
    public Product create(CreateProductReq createProductReq) {
        var existedCategoryOptional = categoryRepository.findById(createProductReq.getCategoryId()) ;
        if (existedCategoryOptional.isEmpty()) {
            throw new ApplicationException("category not found") ;
        }

        Product creatingProduct = productMapper.fromCreateRequest(createProductReq) ;
        creatingProduct.setIsDeleted(false);

        // jpa auditiung

//     creatingProduct.setCreatedDate(Instant.now());
//     creatingProduct.setLastModifiedDate(Instant.now());
        return productRepository.save(creatingProduct) ;

    }

    @Override
    @Cacheable(value = "products", key = "#productFilter.ids" , condition = "#productFilter.ids != null" )
    public List<Product> search(ProductFilterReq productFilter) {
        return productRepository.findByIdIn(productFilter.getIds()) ;
    }

    @Transactional
    @Override
    public String lockProducts(List<ProductLockReq> productLockReq) {

        // instance 1: => update product : 1,2 => key : product : 1,2
        // instance 2: => update product : 2,1 => key : product : 2,1
        // phải sort vì nếu 2 request cùng lock 2 productId giống nhau nhưng thứ tự khác nhau thì sẽ bị deadlock

        // Sử dụng redis distributed lock để đảm bảo chỉ có 1 instance của service có thể lock cùng 1 productId tại cùng 1 thời điểm thay cho getForUpdate của database
        List<String> sortedIds = productLockReq.stream()
                .map(req -> req.getProductId())
                .sorted()
                .collect(Collectors.toList()) ;

        String lockKeyPrefix = "lock:product:"  + String.join("," , sortedIds) ; // tạo String key lock
        RLock lock = redissonClient.getLock(lockKeyPrefix) ; // Tạo object đại diện cho lock ở phía spring

        try {
            //Kiểm tra key lock đó đã bị giữ chưa
            //Nếu chưa có ai giữ → tạo key lock trong Redis và lock thành công và sử dụng trong 5 giây, sau 5 giây Redis sẽ tự động giải phóng lock đó
            //Nếu đã có người giữ → chờ tối đa 10 giây để thử lấy lại lock
            if (lock.tryLock(10,5, TimeUnit.SECONDS)) {
                Thread.sleep(4000); // giả lập thời gian xử lý công việc là 4s
                log.info("Acquired lock for key: {}", lockKeyPrefix);

                List<String> productLockReqIds = productLockReq.stream().map(req -> req.getProductId()).toList() ;
                Map<String,ProductLockReq> productLockReqMap = new HashMap<>() ;

                productLockReq.forEach(productLockItem -> {;
                    productLockReqMap.put(productLockItem.getProductId(), productLockItem) ;
                });

                List<Product> products = productRepository.findByIdIn(productLockReqIds);
                log.info("Products to lock: {}", products.stream().map(Product::getId).collect(Collectors.toList()));

                if (products.size() != productLockReqIds.size()) {
                    throw new ApplicationException("Some products not found");
                }

                for(Product product : products) {
                    product.setStock(product.getStock() - productLockReqMap.get(product.getId()).getQuantity() );
                }
                productRepository.saveAll(products) ;
            } else  { // không lấy được lock thì chạy vào đây
                throw new RuntimeException("Server busy , please try again later") ;
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Process was interrupted while waiting for lock");
        } finally {
            // giải phóng lock sau khi đã xử lý xong
            if(lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("Unlock success for [{}]" , lockKeyPrefix);
            }
        }


    // Lock bằng DB Lock
//        List<String> productLockReqIds = productLockReq.stream().map(req -> req.getProductId()).toList() ;
//        Map<String,ProductLockReq> productLockReqMap = new HashMap<>() ;
//
//        productLockReq.forEach(productLockItem -> {;
//            productLockReqMap.put(productLockItem.getProductId(), productLockItem) ;
//        });
//
//        List<Product> products = productRepository.findByIdInForUpdate(productLockReqIds);
//
//        if (products.size() != productLockReqIds.size()) {
//            throw new ApplicationException("Some products not found");
//        }
//
//        for(Product product : products) {
//            product.setStock(product.getStock() - productLockReqMap.get(product.getId()).getQuantity() );
//        }
//        productRepository.saveAll(products) ;
         return "lock products success" ;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public Product update(String id, UpdateProductReq updateProductReq) {
        var existedProductOptional = productRepository.findById(id) ;
        if (existedProductOptional.isEmpty()) {
            throw new ApplicationException("product not found") ;
        }
        var existedProduct = existedProductOptional.get() ;

        if (updateProductReq.getCategoryId() != null) {
            var existedCategoryOptional = categoryRepository.findById(updateProductReq.getCategoryId()) ;
            if (existedCategoryOptional.isEmpty()) {
                throw new ApplicationException("category not found") ;
            }
        }

        productMapper.fromUpdateRequest(existedProduct,updateProductReq) ;

        return productRepository.save(existedProduct) ;
    }

//    Map<String,Product> productMap = new HashMap<>() ;


    @Override
    public Product getById(String id) {
//        if (productMap.containsKey(id)) {
//            log.info("Get product from cache with id: {}", id);
//            return productMap.get(id) ;
//        }
        Product product = productRepository.findById(id).orElseThrow(() -> new ApplicationException("Product not found")) ;
//        productMap.put(id , product) ;
        return product ;

    }
}
