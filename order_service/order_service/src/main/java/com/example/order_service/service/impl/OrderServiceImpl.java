package com.example.order_service.service.impl;

import com.example.order_service.dto.request.CreateOrderReq;
import com.example.order_service.entity.Order;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository ;
    private final OrderMapper orderMapper ;


    @Override
    public Order create(CreateOrderReq createOrderReq) {
        Order creatingOrder = orderMapper.fromCreateRequest(createOrderReq) ;
        return orderRepository.save(creatingOrder) ;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll() ;
    }

    @Override
    public Order getById(String id) {
        return orderRepository.findById(id).orElse(null) ;
    }

    @Override
    public String deleteById(String id) {
       try {
           orderRepository.deleteById(id);
       } catch (Exception e) {

       }
       return "Deleted successfully" ;
    }


}
