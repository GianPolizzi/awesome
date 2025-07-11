package com.pizza.awesome.model.dto;

import com.pizza.awesome.model.entity.OrderDetailEntity;
import com.pizza.awesome.model.entity.PizzaEntity;
import com.pizza.awesome.utils.OrderStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrderResponseDto {

    private Long orderCode;

    private OrderStatus orderStatus;

    private Double totalPrice;

    private String currency;

    private List<OrderResponseDetailDto> pizzaDetails;
}
