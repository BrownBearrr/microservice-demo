package com.example.order_service.mapper;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order fromCreateRequest(OrderDTO req) ;
}
