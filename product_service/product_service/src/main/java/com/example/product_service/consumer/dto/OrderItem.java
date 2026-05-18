package com.example.product_service.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String id ;
    private String orderId ;
    private String productId ;
    private Integer price ;
    private Integer quantity ;
}
