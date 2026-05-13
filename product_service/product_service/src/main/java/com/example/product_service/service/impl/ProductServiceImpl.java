package com.example.product_service.service.impl;

import com.example.product_service.dto.request.CreateProductReq;
import com.example.product_service.dto.request.ProductFilterReq;
import com.example.product_service.dto.request.ProductLockReq;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.ApplicationException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class  ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository ;
    private final CategoryRepository categoryRepository ;
    private final ProductMapper productMapper ;


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
    public List<Product> search(ProductFilterReq productFilter) {
        return productRepository.findByIdIn(productFilter.getIds()) ;
    }

    @Transactional
    @Override
    public String lockProducts(List<ProductLockReq> productLockReq) {
        List<String> productLockReqIds = productLockReq.stream().map(req -> req.getProductId()).toList() ;
        Map<String,ProductLockReq> productLockReqMap = new HashMap<>() ;

        productLockReq.forEach(productLockItem -> {;
            productLockReqMap.put(productLockItem.getProductId(), productLockItem) ;
        });

        List<Product> products = productRepository.findAllById(productLockReqIds);

        if (products.size() != productLockReqIds.size()) {
            throw new ApplicationException("Some products not found");
        }

        for(Product product : products) {
            product.setStock(product.getStock() - productLockReqMap.get(product.getId()).getQuantity() );
        }
        productRepository.saveAll(products) ;
         return "lock products success" ;
    }
}
