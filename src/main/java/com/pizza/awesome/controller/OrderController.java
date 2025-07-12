package com.pizza.awesome.controller;

import com.pizza.awesome.model.dto.*;
import com.pizza.awesome.model.entity.OrderEntity;
import com.pizza.awesome.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "Order", description = "Operations related to the orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Create pizza order",
            description = "Create pizza order and return order ID with status",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order successfully submitted",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateOrderResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input or validation error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Pizza not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    public ResponseEntity<CreateOrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequest){
        OrderEntity newOrder = orderService.createOrder(orderRequest);

        CreateOrderResponseDto createOrderResponseDto = new CreateOrderResponseDto();
        createOrderResponseDto.setOrderCode(newOrder.getId());
        createOrderResponseDto.setOrderStatus(newOrder.getOrderStatus());

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(newOrder.getId())
                .toUri();

        return ResponseEntity.created(location).body(createOrderResponseDto);
    }

    @GetMapping("/status")
    @Operation(summary = "Get order status",
            description = "Get pizza order status only",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateOrderResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    public ResponseEntity<CreateOrderResponseDto> getOrderStatus(@RequestParam Long id){
        OrderEntity orderEntity = orderService.getOrderStatus(id);

        CreateOrderResponseDto createOrderResponseDto = new CreateOrderResponseDto();
        createOrderResponseDto.setOrderCode(id);
        createOrderResponseDto.setOrderStatus(orderEntity.getOrderStatus());

        return ResponseEntity.ok(createOrderResponseDto);
    }

    @GetMapping("/orders-in-pending")
    @Operation(summary = "Get orders in pending",
            description = "Get all pizza orders in status pending ordered by created date ascendant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders in pending found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Orders in pending not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    public ResponseEntity<List<OrderResponseDto>> getOrdersInPending(){
        List<OrderEntity> ordersEntity = orderService.getOrdersInPending();
        List<OrderResponseDto> ordersResponseDto = new ArrayList<>();
        if(!ordersEntity.isEmpty()){
            ordersResponseDto = ordersEntity.stream().map(orderEntity -> {
                OrderResponseDto orderResponseDto = new OrderResponseDto();
                orderResponseDto.setOrderCode(orderEntity.getId());
                orderResponseDto.setOrderStatus(orderEntity.getOrderStatus());
                orderResponseDto.setTotalPrice(orderEntity.getTotalPrice());
                orderResponseDto.setCurrency(orderEntity.getCurrency());

                List<OrderResponseDetailDto> orderResponseDetailsDto = orderEntity.getOrderDetails().stream()
                                .map(orderDetailEntity -> {
                                    OrderResponseDetailDto orderResponseDetailDto = new OrderResponseDetailDto();
                                    orderResponseDetailDto.setPizza(orderDetailEntity.getPizza().getName());
                                    orderResponseDetailDto.setQuantity(orderDetailEntity.getQuantity());
                                    orderResponseDetailDto.setAdditionalInfo(orderDetailEntity.getAdditionalInfo());
                                    return orderResponseDetailDto;
                                }).toList();

                orderResponseDto.setPizzaDetails(orderResponseDetailsDto);
                return orderResponseDto;
            }).toList();
        }
        return ResponseEntity.ok(ordersResponseDto);
    }

    @PutMapping("/status/update")
    public ResponseEntity<CreateOrderResponseDto> updateOrderStatus(){
        OrderEntity order = orderService.updateOrderStatusReady();
        CreateOrderResponseDto createOrderResponseDto = new CreateOrderResponseDto();
        createOrderResponseDto.setOrderCode(order.getId());
        createOrderResponseDto.setOrderStatus(order.getOrderStatus());
        return ResponseEntity.ok(createOrderResponseDto);
    }
}
