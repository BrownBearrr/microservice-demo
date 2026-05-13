package com.example.product_service.service;

import com.example.product_service.dto.request.CreateProductReq;
import com.example.product_service.dto.request.ProductFilterReq;
import com.example.product_service.dto.request.ProductLockReq;
import com.example.product_service.entity.Product;

import java.util.List;

public interface ProductService {
    Product create(CreateProductReq createProductReq) ;
    List<Product> search(ProductFilterReq productFilter) ;
    String lockProducts(List<ProductLockReq> productLockReq) ;
}
