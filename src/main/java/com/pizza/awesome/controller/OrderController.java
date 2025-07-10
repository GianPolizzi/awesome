package com.pizza.awesome.controller;

import com.pizza.awesome.model.dto.CreateOrderResponseDto;
import com.pizza.awesome.model.dto.ErrorResponseDto;
import com.pizza.awesome.model.dto.OrderRequestDto;
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

@RestController
@RequestMapping("/order")
@Tag(name = "Order", description = "Operations related to the orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Create the pizza order",
            description = "Create the pizza order and return the order ID",
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
}
