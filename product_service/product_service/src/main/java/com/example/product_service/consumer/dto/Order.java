package com.example.product_service.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true) // tránh lỗi khi parse các trường không có trong class
public class Order {

    private String id ;

    private String status ;

    private Integer totalAmount ;

    private String customerId ;

}
