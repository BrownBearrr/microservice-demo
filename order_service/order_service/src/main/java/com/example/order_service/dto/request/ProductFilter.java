package com.example.order_service.dto.request;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductFilter {
    private List<String> ids ;
}
