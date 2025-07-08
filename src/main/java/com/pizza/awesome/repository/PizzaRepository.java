package com.pizza.awesome.repository;

import com.pizza.awesome.model.entity.PizzaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository extends JpaRepository<PizzaEntity, Long> {
}
