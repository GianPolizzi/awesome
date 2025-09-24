package com.pizza.awesome.service;

import com.pizza.awesome.builder.PizzaEntityBuilder;
import com.pizza.awesome.exception.ResourceNotFoundException;
import com.pizza.awesome.model.entity.PizzaEntity;
import com.pizza.awesome.repository.PizzaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private PizzaService pizzaService;

    @Test
    void testGetAll_Success() {
        // GIVEN
        List<PizzaEntity> pizzaMenu = PizzaEntityBuilder.buildPizzaMenu();
        when(pizzaRepository.findAll()).thenReturn(pizzaMenu);

        // WHEN
        List<PizzaEntity> pizzas = pizzaService.getAll();

        // THEN
        assertFalse(pizzas.isEmpty());
        assertEquals(2, pizzas.size());
        assertTrue(pizzas.contains(pizzaMenu.get(0)));
        assertTrue(pizzas.contains(pizzaMenu.get(1)));
        verify(pizzaRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_EmptyMenu_ThrowsException() {
        // GIVEN
        when(pizzaRepository.findAll()).thenReturn(List.of());

        // WHEN
        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> pizzaService.getAll());

        // THEN
        assertEquals("Sorry. The Awesome Pizza Menù is empty!", exception.getMessage());
        verify(pizzaRepository, times(1)).findAll();
    }

}