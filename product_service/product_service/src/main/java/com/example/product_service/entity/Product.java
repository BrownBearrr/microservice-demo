package com.example.product_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Entity
@Getter
@Setter 
@Table(name ="products")
public class Product extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    @Column(length = 36)
    private String id ;
    private String name ;
    private Integer price ;
    private Integer stock ;
    @Column(name= "category_id")
    private String categoryId ;
}
