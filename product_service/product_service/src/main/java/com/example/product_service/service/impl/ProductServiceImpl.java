package com.example.product_service.service.impl;

import com.example.product_service.dto.request.CreateProductReq;
import com.example.product_service.dto.request.ProductFilter;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.ApplicationException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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
    public List<Product> search(ProductFilter productFilter) {
        return productRepository.findByIdIn(productFilter.getIds()) ;
    }
}
