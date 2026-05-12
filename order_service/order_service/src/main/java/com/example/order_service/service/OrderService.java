package com.example.order_service.service;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.entity.Order;

import java.util.List;

public interface OrderService {
    Order create(OrderDTO createOrderReq) ;

    List<Order> getAll() ;

    Order getById(String id) ;

    String deleteById(String id) ;

}
