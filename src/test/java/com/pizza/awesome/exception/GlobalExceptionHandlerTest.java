package com.pizza.awesome.exception;

import com.pizza.awesome.model.dto.ErrorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequestMock;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequestMock = mock(WebRequest.class);
    }

    @Test
    void testHandleResourceNotFoundException() {
        // GIVEN
        String errorMessage = "Order with ID: 999 not found.";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);
        String requestPath = "/order/{id}/status";
        when(webRequestMock.getDescription(false)).thenReturn("uri=" + requestPath);

        // WHEN
        ResponseEntity<ErrorResponseDto> responseEntity =
                globalExceptionHandler.handleResourceNotFoundException(exception, webRequestMock);

        // THEN
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ErrorResponseDto errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(requestPath, errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

}