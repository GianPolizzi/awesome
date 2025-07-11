package com.pizza.awesome.model.dto;

import lombok.Data;

@Data
public class OrderResponseDetailDto {

    private String pizza;

    private int quantity;

    private String additionalInfo;

}
