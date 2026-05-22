package com.example.product_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name ="categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id ;
    private String name ;
    @Column(name = "parent_id")
    private String parentId ;
}
