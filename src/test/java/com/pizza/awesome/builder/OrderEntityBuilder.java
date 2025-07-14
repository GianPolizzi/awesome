package com.pizza.awesome.builder;

import com.pizza.awesome.model.entity.OrderDetailEntity;
import com.pizza.awesome.model.entity.OrderEntity;
import com.pizza.awesome.utils.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderEntityBuilder {

    public static List<OrderEntity> buildOrdersEntity(){
        List<OrderEntity> ordersInPending = new ArrayList<>();
        ordersInPending.add(buildOrderEntity());
        return ordersInPending;
    }

    public static OrderEntity buildOrderEntity(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        orderEntity.setInsertOrderDate(LocalDateTime.now());
        orderEntity.setTotalQuantity(2);
        orderEntity.setTotalPrice(12.0);
        orderEntity.setCustomerName("Polizzi");
        orderEntity.setCustomerAddress("Via Garibaldi 1, Catania");
        orderEntity.setCustomerPhone("3279876543");
        orderEntity.setOrderDetails(List.of(buildOrderDetail()));
        return orderEntity;
    }

    private static OrderDetailEntity buildOrderDetail(){
        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        orderDetailEntity.setId(1L);
        orderDetailEntity.setPizza(PizzaEntityBuilder.buildPizza1());
        orderDetailEntity.setQuantity(1);
        return orderDetailEntity;
    }
}
