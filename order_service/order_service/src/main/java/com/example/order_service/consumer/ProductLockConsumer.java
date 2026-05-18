package com.example.order_service.consumer;

import com.example.order_service.entity.Order;
import com.example.order_service.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductLockConsumer {

    private final OrderService orderService ;

    @KafkaListener(topics = "product_lock")
    public void handleChangeStatusOrder(String orderString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Order orderCreateEvent = objectMapper.readValue(orderString , Order.class) ;
        orderService.changeStatus(orderCreateEvent);
    }
}
