package com.pizza.awesome.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


@Entity
@Table(name = "AW_ORDER_DETAIL")
@Data
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id", nullable = false)
    private PizzaEntity pizza;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo;

}

