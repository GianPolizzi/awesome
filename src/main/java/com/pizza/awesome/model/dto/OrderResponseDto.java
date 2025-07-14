package com.pizza.awesome.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderResponseDto extends BaseOrderResponseDto {

    private Double totalPrice;

    private String currency;

    private List<OrderResponseDetailDto> pizzaDetails;
}
