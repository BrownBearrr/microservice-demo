package com.example.product_service.service;

import com.example.product_service.dto.request.CreateProductReq;
import com.example.product_service.entity.Product;

public interface ProductService {
    Product create(CreateProductReq createProductReq) ;
}
