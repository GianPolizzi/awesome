package com.pizza.awesome.controller;

import com.pizza.awesome.builder.PizzaEntityBuilder;
import com.pizza.awesome.exception.ResourceNotFoundException;
import com.pizza.awesome.model.entity.PizzaEntity;
import com.pizza.awesome.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PizzaController.class)
public class PizzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PizzaService pizzaService;

    @Test
    void testGetPizzaMenu_Success() throws Exception {
        // GIVEN
        List<PizzaEntity> pizzas = PizzaEntityBuilder.buildPizzaMenu();
        when(pizzaService.getAll()).thenReturn(pizzas);

        // THEN
        mockMvc.perform(get("/menu/getAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3L))
                .andExpect(jsonPath("$[0].name").value("Margherita"))
                .andExpect(jsonPath("$[1].id").value(4L))
                .andExpect(jsonPath("$[1].name").value("Patapizza"));

    }

    @Test
    void testGetPizzaMenu_NotFound() throws Exception {
        // GIVEN
        when(pizzaService.getAll())
                .thenThrow(new ResourceNotFoundException("Sorry. The Awesome Pizza Menù is empty!"));

        // THEN
        mockMvc.perform(get("/menu/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}