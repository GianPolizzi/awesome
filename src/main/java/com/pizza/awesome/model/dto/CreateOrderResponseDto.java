package com.pizza.awesome.model.dto;

import com.pizza.awesome.utils.OrderStatus;
import lombok.Data;

@Data
public class CreateOrderResponseDto {

    private Long orderCode;

    private OrderStatus orderStatus;
}
