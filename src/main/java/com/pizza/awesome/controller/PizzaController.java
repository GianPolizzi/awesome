package com.pizza.awesome.controller;

import com.pizza.awesome.model.entity.PizzaEntity;
import com.pizza.awesome.service.PizzaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Tag(name = "Menu Awesome Pizza", description = "Operations related to the pizza menu")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @GetMapping("/show")
    @Operation(summary = "Get all pizzas on the menu",
            description = "Returns a list of all available pizzas with ingrdients",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PizzaEntity.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<PizzaEntity>> getMenuPizza(){
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }
}
