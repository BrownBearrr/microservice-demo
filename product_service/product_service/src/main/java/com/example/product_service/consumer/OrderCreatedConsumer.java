package com.example.product_service.consumer;

import com.example.product_service.consumer.dto.Order;
import com.example.product_service.consumer.dto.OrderCreateEvent;
import com.example.product_service.dto.request.ProductLockReq;
import com.example.product_service.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final ObjectMapper objectMapper ;
    private final ProductService productService ;
    private final KafkaTemplate<String,Object> kafkaTemplate ;


    @KafkaListener(topics = "order_created")
    @RetryableTopic(
            attempts = "4" , // số lần retry
            backoff = @Backoff(delay = 2000 , multiplier = 2.0) , // delay theo hệ số nhân
            exclude = {NullPointerException.class , IllegalArgumentException.class } // Các exception k cần retry
    )
    public void handleOrderCreatedEvent(String orderString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderCreateEvent orderCreateEvent = objectMapper.readValue(orderString , OrderCreateEvent.class) ;

//        if (orderCreateEvent != null) {
//            throw new RuntimeException("Test RetryableTopic");
//        }
        log.info("Received order created event: {}", orderCreateEvent);

        List<ProductLockReq> lockProductItems = new ArrayList<>() ;

        orderCreateEvent.getOrderItems().forEach(orderItem -> {
            ProductLockReq lockProductItem = new ProductLockReq() ;
            lockProductItem.setProductId(orderItem.getProductId());
            lockProductItem.setQuantity(orderItem.getQuantity());
            lockProductItems.add(lockProductItem) ;
        });

        productService.lockProducts(lockProductItems) ;

        log.info("Success to lock product item of {}" , orderCreateEvent.getId());
        kafkaTemplate.send("product_lock", orderCreateEvent) ; // gửi message sang kafka topic "order_created" với payload là createdOrder

    }

}
