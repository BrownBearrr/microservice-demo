package com.example.order_service.mapper;

import com.example.order_service.dto.OrderItemDTO;
import com.example.order_service.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem fromCreateRequest(OrderItemDTO req) ;
}
