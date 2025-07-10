package com.pizza.awesome.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDetailRequestDto {

    @NotNull(message = "Pizza ID cannot be null")
    @Min(value = 1, message = "Pizza ID must be greater than 0")
    private Long pizzaId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String variations;
}
