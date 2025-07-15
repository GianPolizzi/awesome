package com.pizza.awesome.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderDetailRequestDto {

    @NotNull(message = "Pizza ID cannot be null")
    @Min(value = 1, message = "Pizza ID must be greater than 0")
    private Long pizzaId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @JsonProperty("additional_info")
    private String additionalInfo;
}
