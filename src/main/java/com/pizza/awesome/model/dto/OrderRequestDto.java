package com.pizza.awesome.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer address is required")
    private String customerAddress;

    @NotBlank(message = "Customer phone is required")
    private String customerPhone;

    @JsonProperty(namespace = "order_details")
    @NotEmpty(message = "Order must contain at least one pizza")
    @Valid
    private List<OrderDetailRequestDto> orderDetailsDto;
}
