package com.pizza.awesome.service;

import com.pizza.awesome.exception.ResourceNotFoundException;
import com.pizza.awesome.model.entity.PizzaEntity;
import com.pizza.awesome.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    public List<PizzaEntity> getAllPizzas(){
        List<PizzaEntity> pizzas = pizzaRepository.findAll();
        if(pizzas.isEmpty()){
            throw new ResourceNotFoundException("Sorry. The Awesome Pizza Menù is empty!");
        }
        return pizzas;
    }

}
