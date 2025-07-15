package com.pizza.awesome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizza.awesome.builder.OrderEntityBuilder;
import com.pizza.awesome.builder.OrderRequestBuilder;
import com.pizza.awesome.builder.OrderResponseBuilder;
import com.pizza.awesome.exception.ResourceNotFoundException;
import com.pizza.awesome.model.dto.OrderRequestDto;
import com.pizza.awesome.model.dto.OrderResponseDto;
import com.pizza.awesome.model.entity.OrderEntity;
import com.pizza.awesome.service.OrderService;
import com.pizza.awesome.utils.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;


    @Test
    void testCreateOrder_Success() throws Exception {
        // GIVEN
        OrderEntity mockOrderEntity = OrderEntityBuilder.buildOrderEntity();
        when(orderService.createOrder(any(OrderRequestDto.class))).thenReturn(mockOrderEntity);

        // THEN
        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderRequestBuilder.buildOrderRequestDto())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.orderCode").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("PENDING"));
    }

    @Test
    void testCreateOrder_ValidationFailure() throws Exception {
        // GIVEN
        OrderRequestDto invalidRequest = OrderRequestBuilder.buildOrderRequestDto();
        invalidRequest.setCustomerName(null);

        // THEN
        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOrder_PizzaNotFound() throws Exception {
        // GIVEN
        when(orderService.createOrder(any(OrderRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Pizza not found"));

        // THEN
        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderRequestBuilder.buildOrderRequestDto())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pizza not found"));
    }

    @Test
    void testGetOrderStatusById_Success() throws Exception {
        // GIVEN
        Long orderId = 1L;
        OrderEntity mockOrderEntity = OrderEntityBuilder.buildOrderEntity();
        when(orderService.getOrderStatus(orderId)).thenReturn(mockOrderEntity);

        // THEN
        mockMvc.perform(get("/order/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value(orderId))
                .andExpect(jsonPath("$.orderStatus").value("PENDING"));
    }

    @Test
    void testGetOrderStatusById_NotFound() throws Exception {
        // GIVEN
        Long orderId = 999L;
        when(orderService.getOrderStatus(orderId))
                .thenThrow(new ResourceNotFoundException("Order with ID 999 not found."));

        // THEN
        mockMvc.perform(get("/order/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order with ID 999 not found."));
    }

    @Test
    void testGetOrdersInPendingStatus_Success() throws Exception {
        // GIVEN
        OrderResponseDto pendingOrderDto = OrderResponseBuilder.buildOrderResponseDto();
        List<OrderResponseDto> pendingOrders = List.of(pendingOrderDto);
        when(orderService.getOrdersInPendingStatus()).thenReturn(pendingOrders);

        // THEN
        mockMvc.perform(get("/order/orders-in-pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderCode").value(1L));
    }

    @Test
    void testGetOrdersInPendingStatus_EmptyList() throws Exception {
        // GIVEN
        when(orderService.getOrdersInPendingStatus()).thenReturn(Collections.emptyList());

        // THEN
        mockMvc.perform(get("/order/orders-in-pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testUpdateOrderStatus_Success() throws Exception {
        // GIVEN
        OrderEntity updatedOrder = OrderEntityBuilder.buildOrderEntity();
        updatedOrder.setOrderStatus(OrderStatus.IN_PROCESS);
        when(orderService.updateOrderNextStatus()).thenReturn(updatedOrder);

        // THEN
        mockMvc.perform(put("/order/status/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("IN_PROCESS"));
    }

    @Test
    void testUpdateOrderStatus_NoOrderFound() throws Exception {
        // GIVEN
        when(orderService.updateOrderNextStatus()).thenThrow(new ResourceNotFoundException("There isn't any order to process!"));

        // THEN
        mockMvc.perform(put("/order/status/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("There isn't any order to process!"));
    }
}