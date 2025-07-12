package com.pizza.awesome.service;

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
        return pizzaRepository.findAll();
    }

}
