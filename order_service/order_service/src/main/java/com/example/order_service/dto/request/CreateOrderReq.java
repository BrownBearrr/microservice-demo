package com.example.order_service.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderReq {
    @NotEmpty
    private String status ;
    @NotNull
    @Positive
    private Integer totalAmount ;
    @NotEmpty
    private String customerId ;
}
