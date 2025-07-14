package com.pizza.awesome.builder;

import com.pizza.awesome.model.dto.OrderResponseDto;
import com.pizza.awesome.utils.Constants;
import com.pizza.awesome.utils.OrderStatus;

import java.util.List;

public class OrderResponseBuilder {

    public static OrderResponseDto buildOrderResponseDto(){
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderCode(1L);
        orderResponseDto.setOrderStatus(OrderStatus.PENDING);
        orderResponseDto.setTotalPrice(20.00);
        orderResponseDto.setCurrency(Constants.EURO);
        orderResponseDto.setPizzaDetails(List.of());
        return orderResponseDto;
    }
}
