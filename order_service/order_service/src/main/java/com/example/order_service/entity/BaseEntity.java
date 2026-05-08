package com.example.order_service.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BaseEntity {
    private Boolean isDeleted ;
    private Instant createdDate ;
    private String createdBy ;
    private Instant lastModifiedDate ;
    private String lastModifiedBy ;
}

