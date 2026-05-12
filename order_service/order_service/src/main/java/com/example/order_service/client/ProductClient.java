package com.example.order_service.client;

import com.example.order_service.dto.ProductDTO;
import com.example.order_service.dto.request.ProductFilter;

import java.util.List;

public interface ProductClient {
    List<ProductDTO> getProductsByIds(ProductFilter productFilter) ;
}
