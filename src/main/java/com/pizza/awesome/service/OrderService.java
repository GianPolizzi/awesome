package com.pizza.awesome.service;

import com.pizza.awesome.exception.ResourceNotFoundException;
import com.pizza.awesome.model.dto.OrderDetailRequestDto;
import com.pizza.awesome.model.dto.OrderRequestDto;
import com.pizza.awesome.model.dto.OrderResponseDetailDto;
import com.pizza.awesome.model.dto.OrderResponseDto;
import com.pizza.awesome.model.entity.OrderDetailEntity;
import com.pizza.awesome.model.entity.OrderEntity;
import com.pizza.awesome.model.entity.PizzaEntity;
import com.pizza.awesome.repository.OrderRepository;
import com.pizza.awesome.repository.PizzaRepository;
import com.pizza.awesome.utils.Constants;
import com.pizza.awesome.utils.OrderDetailsProcessingResult;
import com.pizza.awesome.utils.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Transactional
    public OrderEntity createOrder(OrderRequestDto orderRequestDto){

        OrderDetailsProcessingResult orderDetailsProcessed = getOrderDetails(orderRequestDto.getOrderDetailsDto());
        OrderEntity orderEntity = getOrderEntity(orderRequestDto, orderDetailsProcessed);
        orderDetailsProcessed.getOrderDetailsEntity()
                .forEach(orderDetailEntity -> orderDetailEntity.setOrder(orderEntity));
        orderEntity.setOrderDetails(orderDetailsProcessed.getOrderDetailsEntity());

        return orderRepository.save(orderEntity);
    }

    public OrderEntity getOrderStatus(Long id){
        Optional<OrderEntity> orderEntityOpt = orderRepository.findById(id);
        if(orderEntityOpt.isEmpty()){
            throw new ResourceNotFoundException("Order with ID: " + id + " not found.");
        }
        return orderEntityOpt.get();
    }

    public List<OrderResponseDto> getOrdersInPendingStatus(){
        List<OrderEntity> ordersInPending = orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
        List<OrderResponseDto> ordersResponseDto = new ArrayList<>();
        if(!ordersInPending.isEmpty()) {
            ordersResponseDto = ordersInPending.stream().map(orderEntity -> {
                OrderResponseDto orderResponseDto = new OrderResponseDto();
                orderResponseDto.setOrderCode(orderEntity.getId());
                orderResponseDto.setOrderStatus(orderEntity.getOrderStatus());
                orderResponseDto.setTotalPrice(orderEntity.getTotalPrice());
                orderResponseDto.setCurrency(orderEntity.getCurrency());

                List<OrderResponseDetailDto> orderResponseDetailsDto = orderEntity.getOrderDetails().stream()
                        .map(orderDetailEntity -> {
                            OrderResponseDetailDto orderResponseDetailDto = new OrderResponseDetailDto();
                            orderResponseDetailDto.setPizza(orderDetailEntity.getPizza().getName());
                            orderResponseDetailDto.setQuantity(orderDetailEntity.getQuantity());
                            orderResponseDetailDto.setAdditionalInfo(orderDetailEntity.getAdditionalInfo());
                            return orderResponseDetailDto;
                        }).toList();

                orderResponseDto.setPizzaDetails(orderResponseDetailsDto);
                return orderResponseDto;
            }).toList();
        }
        return ordersResponseDto;
    }

    @Transactional
    public OrderEntity updateOrderNextStatus(){
        Optional<OrderEntity> orderToUpdate = findNextOrder(OrderStatus.IN_PROCESS);

        if (orderToUpdate.isEmpty()) {
            orderToUpdate = findNextOrder(OrderStatus.PENDING);
        }

        OrderEntity order = orderToUpdate.orElseThrow(
                () -> new ResourceNotFoundException("There isn't any order to process!")
        );

        if (order.getOrderStatus() == OrderStatus.IN_PROCESS) {
            order.setOrderStatus(OrderStatus.READY);
        } else {
            order.setOrderStatus(OrderStatus.IN_PROCESS);
        }
        return order;
    }

    private OrderDetailsProcessingResult getOrderDetails(List<OrderDetailRequestDto> orderDetailsRequestDto){

        AtomicInteger pizzaTotalQuantity = new AtomicInteger(0);
        DoubleAdder pizzaTotalPrice = new DoubleAdder();

        List<OrderDetailEntity> orderDetailsEntity =
                orderDetailsRequestDto.stream()
                        .map(orderDetailDto -> {
                            PizzaEntity pizzaEntity = pizzaRepository.findById(orderDetailDto.getPizzaId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Pizza with ID " + orderDetailDto.getPizzaId() + " not found."));

                            double pizzaDetailTotalPrice = pizzaEntity.getPrice() * orderDetailDto.getQuantity();

                            pizzaTotalQuantity.addAndGet(orderDetailDto.getQuantity());
                            pizzaTotalPrice.add(pizzaDetailTotalPrice);

                            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                            orderDetailEntity.setPizza(pizzaEntity);
                            orderDetailEntity.setQuantity(orderDetailDto.getQuantity());
                            orderDetailEntity.setAdditionalInfo(orderDetailDto.getVariations());

                            return orderDetailEntity;
                        }).collect(Collectors.toList());
        return new OrderDetailsProcessingResult(orderDetailsEntity, pizzaTotalQuantity, pizzaTotalPrice);
    }

    private OrderEntity getOrderEntity(OrderRequestDto orderRequestDto, OrderDetailsProcessingResult orderDetailsProcessed){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerName(orderRequestDto.getCustomerName());
        orderEntity.setCustomerAddress(orderRequestDto.getCustomerAddress());
        orderEntity.setCustomerPhone(orderRequestDto.getCustomerPhone());
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        orderEntity.setInsertOrderDate(LocalDateTime.now());
        orderEntity.setTotalQuantity(orderDetailsProcessed.getPizzaTotalQuantity().get());
        orderEntity.setTotalPrice(orderDetailsProcessed.getPizzaTotalPrice().sum());
        orderEntity.setCurrency(Constants.EURO);
        return orderEntity;
    }

    private Optional<OrderEntity> findNextOrder(OrderStatus status) {
        List<OrderEntity> orders = orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(status);
        return orders.stream().findFirst();
    }
}
