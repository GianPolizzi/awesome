package com.pizza.awesome.utils;

import com.pizza.awesome.model.entity.OrderDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;

@Getter
@AllArgsConstructor
public class OrderDetailsProcessingResult {

    private final List<OrderDetailEntity> orderDetailsEntity;
    private final AtomicInteger pizzaTotalQuantity;
    private final DoubleAdder pizzaTotalPrice;

}
