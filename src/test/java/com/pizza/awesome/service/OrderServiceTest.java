package com.pizza.awesome.service;

import com.pizza.awesome.builder.OrderEntityBuilder;
import com.pizza.awesome.builder.OrderRequestBuilder;
import com.pizza.awesome.builder.PizzaEntityBuilder;
import com.pizza.awesome.exception.ResourceNotFoundException;
import com.pizza.awesome.model.dto.OrderRequestDto;
import com.pizza.awesome.model.dto.OrderResponseDto;
import com.pizza.awesome.model.entity.OrderEntity;
import com.pizza.awesome.model.entity.PizzaEntity;
import com.pizza.awesome.repository.OrderRepository;
import com.pizza.awesome.repository.PizzaRepository;
import com.pizza.awesome.utils.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder_Success() {
        // GIVEN
        OrderRequestDto mockOrderRequestDto = OrderRequestBuilder.buildOrderRequestDto();
        OrderEntity mockPendingOrder = OrderEntityBuilder.buildOrderEntity();
        PizzaEntity mockPizzaiola = PizzaEntityBuilder.buildPizza1();
        when(pizzaRepository.findById(anyLong())).thenReturn(Optional.of(mockPizzaiola));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(mockPendingOrder);

        // WHEN
        OrderEntity createdOrder = orderService.createOrder(mockOrderRequestDto);

        // THEN
        assertNotNull(createdOrder);
        assertEquals(mockPendingOrder.getId(), createdOrder.getId());
        assertEquals(OrderStatus.PENDING, createdOrder.getOrderStatus());
        assertEquals("Polizzi", createdOrder.getCustomerName());
        assertEquals(12.00, createdOrder.getTotalPrice());
        assertEquals(2, createdOrder.getTotalQuantity());
        assertFalse(createdOrder.getOrderDetails().isEmpty());
        verify(pizzaRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testCreateOrder_PizzaNotFound() {
        // GIVEN
        OrderRequestDto mockOrderRequestDto = OrderRequestBuilder.buildOrderRequestDto();
        when(pizzaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // WHEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(mockOrderRequestDto);
        });

        // THEN
        assertEquals("Pizza with ID " + mockOrderRequestDto.getOrderDetailsDto().get(0).getPizzaId() + " not found.", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
        verify(pizzaRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetOrderStatus_Success() {
        // GIVEN
        OrderEntity mockPendingOrder = OrderEntityBuilder.buildOrderEntity();
        when(orderRepository.findById(mockPendingOrder.getId())).thenReturn(Optional.of(mockPendingOrder));

        // WHEN
        OrderEntity resultOrder = orderService.getOrderStatus(mockPendingOrder.getId());

        // THEN
        assertNotNull(resultOrder);
        assertEquals(mockPendingOrder.getId(), resultOrder.getId());
        assertEquals(OrderStatus.PENDING, resultOrder.getOrderStatus());
        verify(orderRepository, times(1)).findById(mockPendingOrder.getId());
    }

    @Test
    void testGetOrderStatus_NotFound() {
        // GIVEN
        Long notExistentId = 99L;
        when(orderRepository.findById(notExistentId)).thenReturn(Optional.empty());

        // WHEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderStatus(notExistentId);
        });

        // THEN
        assertEquals("Order with ID: " + notExistentId + " not found.", exception.getMessage());
        verify(orderRepository, times(1)).findById(notExistentId);
    }

    @Test
    void testGetOrdersInPendingStatus_Success() {
        // GIVEN
        List<OrderEntity> ordersInPending = OrderEntityBuilder.buildOrdersEntity();
        when(orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING))
                .thenReturn(ordersInPending);

        // WHEN
        List<OrderResponseDto> ordersInPendingStatus = orderService.getOrdersInPendingStatus();

        // THEN
        assertNotNull(ordersInPendingStatus);
        assertFalse(ordersInPendingStatus.isEmpty());
        assertEquals(1, ordersInPendingStatus.size());
        assertEquals(ordersInPending.get(0).getId(), ordersInPendingStatus.get(0).getOrderCode());
        assertEquals(OrderStatus.PENDING, ordersInPendingStatus.get(0).getOrderStatus());
        assertEquals(ordersInPending.get(0).getTotalPrice(), ordersInPendingStatus.get(0).getTotalPrice());
        assertFalse(ordersInPendingStatus.get(0).getPizzaDetails().isEmpty());
        assertEquals("Pizzaiola", ordersInPendingStatus.get(0).getPizzaDetails().get(0).getPizza());
        assertEquals(1, ordersInPendingStatus.get(0).getPizzaDetails().get(0).getQuantity());
        verify(orderRepository, times(1)).findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
    }

    @Test
    void testGetOrdersInPendingStatus_EmptyList() {
        // GIVEN
        when(orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING))
                .thenReturn(List.of());

        // WHEN
        List<OrderResponseDto> result = orderService.getOrdersInPendingStatus();

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
    }

    @Test
    void testUpdateOrderNextStatus_InProcessToReady() {
        // GIVEN
        OrderEntity orderInProcess = OrderEntityBuilder.buildOrderEntity();
        orderInProcess.setOrderStatus(OrderStatus.IN_PROCESS);
        when(orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.IN_PROCESS))
                .thenReturn(List.of(orderInProcess));

        // WHEN
        OrderEntity updatedOrder = orderService.updateOrderNextStatus();

        // THEN
        assertNotNull(updatedOrder);
        assertEquals(orderInProcess.getId(), updatedOrder.getId());
        assertEquals(OrderStatus.READY, updatedOrder.getOrderStatus());
        verify(orderRepository, times(1)).findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.IN_PROCESS);
        verify(orderRepository, never()).findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
    }

    @Test
    void testUpdateOrderNextStatus_PendingToInProcess() {
        // GIVEN
        OrderEntity orderInPending = OrderEntityBuilder.buildOrderEntity();
        when(orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.IN_PROCESS))
                .thenReturn(List.of());
        when(orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING))
                .thenReturn(List.of(orderInPending));

        // WHEN
        OrderEntity updatedOrder = orderService.updateOrderNextStatus();

        // THEN
        assertNotNull(updatedOrder);
        assertEquals(orderInPending.getId(), updatedOrder.getId());
        assertEquals(OrderStatus.IN_PROCESS, updatedOrder.getOrderStatus());
        verify(orderRepository, times(1)).findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.IN_PROCESS);
        verify(orderRepository, times(1)).findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
    }

    @Test
    void testUpdateOrderNextStatus_NoOrderToProcess() {
        // GIVEN
        when(orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.IN_PROCESS))
                .thenReturn(List.of());
        when(orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING))
                .thenReturn(List.of());

        // WHEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.updateOrderNextStatus();
        });

        // THEN
        assertEquals("There isn't any order to process!", exception.getMessage());
        verify(orderRepository, times(1)).findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.IN_PROCESS);
        verify(orderRepository, times(1)).findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
    }
}