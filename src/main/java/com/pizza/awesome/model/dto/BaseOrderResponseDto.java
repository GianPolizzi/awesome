package com.pizza.awesome.model.dto;

import com.pizza.awesome.utils.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseOrderResponseDto {

    private Long orderCode;

    private OrderStatus orderStatus;
}
