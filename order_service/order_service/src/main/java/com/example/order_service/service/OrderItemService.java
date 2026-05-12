package com.example.order_service.service;

import com.example.order_service.dto.OrderItemDTO;
import com.example.order_service.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
//    OrderItem create(OrderItemDTO createOderItemReq)  ;
    List<OrderItem> getAll () ;
    OrderItem getById(String id) ;
    String delete(String id) ;
}
