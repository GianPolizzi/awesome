package com.pizza.awesome.builder;

import com.pizza.awesome.model.dto.OrderDetailRequestDto;
import com.pizza.awesome.model.dto.OrderRequestDto;

import java.util.List;

public class OrderRequestBuilder {

    public static OrderRequestDto buildOrderRequestDto(){
        return new OrderRequestDto()
                .setCustomerName("Polizzi")
                .setCustomerAddress("Via Garibaldi 1, Catania")
                .setCustomerPhone("3279876543")
                .setOrderDetailsDto(List.of(
                        buildOrderDetailRequestDto()
                ));
    }

    private static OrderDetailRequestDto buildOrderDetailRequestDto(){
        return new OrderDetailRequestDto()
                .setPizzaId(1L)
                .setQuantity(2)
                .setAdditionalInfo("Lactose free");
    }
}
