package com.example.order_service.controller;

import com.example.order_service.dto.BaseResponse;
import com.example.order_service.dto.OrderItemDTO;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService ;

//    @PostMapping
//    public ResponseEntity<BaseResponse<OrderItem>> create( @RequestBody @Valid OrderItemDTO req) {
//        return ResponseEntity.ok().body(new BaseResponse<>(orderItemService.create(req),"create order item success"))  ;
//    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll() {
        return ResponseEntity.ok().body(new BaseResponse<>(orderItemService.getAll(),"get all order item success"))  ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok().body(new BaseResponse<>(orderItemService.getById(id),"get order item by id success"))  ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok().body(new BaseResponse<>(orderItemService.delete(id),"delete order item by id success"))  ;
    }
}
