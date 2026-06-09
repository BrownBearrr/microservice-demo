package com.example.product_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductReq {
    private String name ;
    private Integer price ;
    private Integer stock ;
    private String categoryId ;
}
