package com.example.order_service.service.impl;

import com.example.order_service.dto.request.CreateOderItemReq;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.exception.ApplicationException;
import com.example.order_service.mapper.OrderItemMapper;
import com.example.order_service.repository.OrderItemRepository;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;


    @Override
    public OrderItem create(CreateOderItemReq createOderItemReq) {
        var existedOrderOptional = orderRepository.findById(createOderItemReq.getOrderId());
        if (existedOrderOptional.isEmpty()) {
            throw new ApplicationException("order not found");
        }
        OrderItem creatingOrderItem = orderItemMapper.fromCreateRequest(createOderItemReq);
        creatingOrderItem.setOrder(existedOrderOptional.get());
        return orderItemRepository.save(creatingOrderItem);
    }

    @Override
    public List<OrderItem> getAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem getById(String id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("order item not found"));
    }

    @Transactional
    @Override
    public String delete(String id) {
        OrderItem orderItemDelete = orderItemRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("order item not found"));

        Order order = orderItemDelete.getOrder();

        order.getOrderItems().remove(orderItemDelete);


        return "Deleted successfully";
    }
}
