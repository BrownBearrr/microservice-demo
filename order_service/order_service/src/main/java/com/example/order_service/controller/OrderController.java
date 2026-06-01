package com.example.order_service.controller;

import com.example.order_service.dto.BaseResponse;
import com.example.order_service.dto.OrderDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {
    private final OrderService orderService ;

    @PostMapping
    public ResponseEntity<BaseResponse<Order>> create(@RequestBody @Valid OrderDTO createOrderReq) {
        log.info("Create order request: {}", createOrderReq);
        return ResponseEntity.ok(new BaseResponse<>(orderService.create(createOrderReq) , "create order succeess")) ;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<Order>>> getAll() {
        return ResponseEntity.ok(new BaseResponse<>(orderService.getAll() , "get all order succeess")) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Order>> getById(@PathVariable String id) {
        return ResponseEntity.ok(new BaseResponse<>(orderService.getById(id) , "get order by id succeess")) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(new BaseResponse<>(orderService.deleteById(id) , "delete order by id succeess")) ;
    }
}
