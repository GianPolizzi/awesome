package com.pizza.awesome.controller;

import com.pizza.awesome.model.entity.MenuEntity;
import com.pizza.awesome.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Tag(name = "Menu", description = "Operations related to the pizza menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/show")
    @Operation(summary = "Get all pizzas on the menu",
            description = "Returns a list of all available pizzas with ingrdients",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public List<MenuEntity> getMenuPizza(){
        return menuService.getMenu();
    }
}
