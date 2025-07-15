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
import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "Order", description = "Operations related to the pizza orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Create order",
            description = "Create order and return order ID with status",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BaseOrderResponseDto.class))),
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
    public ResponseEntity<BaseOrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequest){
        OrderEntity newOrder = orderService.createOrder(orderRequest);
        long orderId = newOrder.getId();

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(orderId)
                .toUri();

        return ResponseEntity.created(location).body(new BaseOrderResponseDto(orderId, newOrder.getOrderStatus()));
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Get order status By ID",
            description = "Get pizza order status By ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BaseOrderResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    public ResponseEntity<BaseOrderResponseDto> getOrderStatusById(@PathVariable Long id){
        OrderEntity orderEntity = orderService.getOrderStatus(id);
        return ResponseEntity.ok(new BaseOrderResponseDto(id, orderEntity.getOrderStatus()));
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
    public ResponseEntity<List<OrderResponseDto>> getOrdersInPendingStatus(){
        return ResponseEntity.ok(orderService.getOrdersInPendingStatus());
    }

    @PutMapping("/status/update")
    public ResponseEntity<BaseOrderResponseDto> updateOrderStatus(){
        OrderEntity order = orderService.updateOrderNextStatus();
        return ResponseEntity.ok(new BaseOrderResponseDto(order.getId(), order.getOrderStatus()));
    }
}
