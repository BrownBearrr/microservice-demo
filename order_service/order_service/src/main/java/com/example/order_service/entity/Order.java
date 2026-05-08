package com.example.order_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;


@Entity
@Getter
@Setter
@Table(name ="orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    private String id ;

    private String status ;

    @Column(name = "total_amount")
    private Integer totalAmount ;


    @Column(name = "customer_id")
    private String customerId ;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    List<OrderItem> orderItems ;

}


