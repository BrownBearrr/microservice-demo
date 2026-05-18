package com.example.order_service.events;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreatedEvent extends Order {
    private List<OrderItem> orderItems ;
}
