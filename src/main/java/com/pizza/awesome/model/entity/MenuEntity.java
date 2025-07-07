package com.pizza.awesome.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MENU")
@Data
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String ingredients;

    private Double price;
}
