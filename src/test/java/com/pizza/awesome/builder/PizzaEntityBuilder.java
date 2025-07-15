package com.pizza.awesome.builder;

import com.pizza.awesome.model.entity.PizzaEntity;

import java.util.ArrayList;
import java.util.List;

public class PizzaEntityBuilder {

    public static List<PizzaEntity> buildPizzaMenu(){
        List<PizzaEntity> pizzas = new ArrayList<>();
        pizzas.add(buildPizza3());
        pizzas.add(buildPizza4());
        return pizzas;
    }

    private static PizzaEntity buildPizza3(){
        PizzaEntity margherita = new PizzaEntity();
        margherita.setId(3L);
        margherita.setName("Margherita");
        margherita.setIngredients("pomodoro, mozzarella, olio, origano, basilico");
        margherita.setPrice(7.0);
        margherita.setCurrency("EUR");
        return margherita;
    }

    private static PizzaEntity buildPizza4(){
        PizzaEntity patapizza = new PizzaEntity();
        patapizza.setId(4L);
        patapizza.setName("Patapizza");
        patapizza.setIngredients("pomodoro, mozzarella, wustel, patatine, olio, origano, basilico");
        patapizza.setPrice(9.0);
        patapizza.setCurrency("EUR");
        return patapizza;
    }

    public static PizzaEntity buildPizza1(){
        PizzaEntity pizzaiola = new PizzaEntity();
        pizzaiola.setId(1L);
        pizzaiola.setName("Pizzaiola");
        pizzaiola.setIngredients("pomodoro, olio, origano, basilico");
        pizzaiola.setPrice(6.0);
        pizzaiola.setCurrency("EUR");
        return pizzaiola;
    }
}
