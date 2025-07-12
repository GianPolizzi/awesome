package com.pizza.awesome.exception;

import com.pizza.awesome.model.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {

        ErrorResponseDto errorDetails = new ErrorResponseDto();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetails.setError("Not Found");
        errorDetails.setPath(request.getDescription(false).replace("uri=", ""));
        errorDetails.setMessage(e.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
