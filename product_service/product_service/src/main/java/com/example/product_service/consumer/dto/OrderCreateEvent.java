package com.example.product_service.consumer.dto;

import com.example.product_service.dto.request.ProductLockReq;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCreateEvent extends Order {
    private List<OrderItem> orderItems ;
}
