package com.example.product_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductLockReq {
    private String productId ;
    private Integer quantity ;
}
