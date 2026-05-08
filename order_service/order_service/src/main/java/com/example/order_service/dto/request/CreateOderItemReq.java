package com.example.order_service.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOderItemReq {
    @NotNull
    @Positive
    private Integer quantity ;

    @NotNull
    @Positive
    private Integer price ;

    @NotEmpty
    private String orderId ;
    @NotEmpty
    private String productId ;
}
