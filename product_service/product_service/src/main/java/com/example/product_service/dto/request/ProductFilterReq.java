package com.example.product_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductFilterReq {
    @NotEmpty
    private List<String> ids ;
}
