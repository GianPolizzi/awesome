package com.pizza.awesome.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDto {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String path;
    private String message;
}
