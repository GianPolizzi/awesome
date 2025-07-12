package com.pizza.awesome.repository;

import com.pizza.awesome.model.entity.OrderEntity;
import com.pizza.awesome.utils.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus orderStatus);
}
