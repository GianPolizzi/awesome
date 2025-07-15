package com.pizza.awesome.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AW_PIZZA")
@Data
public class PizzaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ingredients", nullable = false)
    private String ingredients;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "currency", nullable = false)
    private String currency;
}
