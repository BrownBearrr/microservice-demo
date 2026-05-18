package com.example.order_service.mapper;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.events.OrderCreatedEvent;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order fromCreateRequest(OrderDTO req) ;
    OrderDTO toDTO(Order order) ;
    OrderCreatedEvent toEvent(Order order ) ;
}
