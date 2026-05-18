package com.example.order_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@Table(name ="order_items")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {
    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    private String id ;
    private Integer quantity ;
    private Integer price ;

    @Column(name = "product_id")
    private String productId ;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order ;
}
