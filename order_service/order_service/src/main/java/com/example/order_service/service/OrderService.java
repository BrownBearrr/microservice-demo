package com.example.order_service.service;

import com.example.order_service.dto.request.CreateOrderReq;
import com.example.order_service.entity.Order;

import java.util.List;

public interface OrderService {
    Order create(CreateOrderReq createOrderReq) ;

    List<Order> getAll() ;

    Order getById(String id) ;

    String deleteById(String id) ;

}
