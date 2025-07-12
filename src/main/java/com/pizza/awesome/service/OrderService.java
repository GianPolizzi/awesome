package com.pizza.awesome.service;

import com.pizza.awesome.exception.ResourceNotFoundException;
import com.pizza.awesome.model.dto.OrderDetailRequestDto;
import com.pizza.awesome.model.dto.OrderRequestDto;
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

        OrderDetailsProcessingResult result = processOrderDetails(orderRequestDto.getOrderDetailsDto());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerName(orderRequestDto.getCustomerName());
        orderEntity.setCustomerAddress(orderRequestDto.getCustomerAddress());
        orderEntity.setCustomerPhone(orderRequestDto.getCustomerPhone());
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        orderEntity.setInsertOrderDate(LocalDateTime.now());
        orderEntity.setTotalQuantity(result.getPizzaTotalQuantity().get());
        orderEntity.setTotalPrice(result.getPizzaTotalPrice().sum());
        orderEntity.setCurrency(Constants.EURO);

        // 3. Collega i Dettagli all'Ordine e viceversa
        // Imposta la relazione bidirezionale Order <-> OrderDetail
        result.getOrderDetailsEntity()
                .forEach(orderDetailEntity -> orderDetailEntity.setOrder(orderEntity));
        orderEntity.setOrderDetails(result.getOrderDetailsEntity()); // Imposta la lista di OrderDetails nell'Order

        // 4. Salva l'Ordine (che casca anche i dettagli)
        return orderRepository.save(orderEntity);
    }

    public OrderEntity getOrderStatus(Long id){
        Optional<OrderEntity> orderEntityOpt = orderRepository.findById(id);
        if(orderEntityOpt.isEmpty()){
            throw new ResourceNotFoundException("Order with ID: " + id + " not found.");
        }
        return orderEntityOpt.get();
    }

    public List<OrderEntity> getOrdersInPending(){
        List<OrderEntity> ordersInPending = orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
        return ordersInPending;
    }

    public void updateOrderStatusInProcess(){
        List<OrderEntity> ordersInProcess = orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
        OrderEntity orderInProcess = Optional.ofNullable(ordersInProcess).orElse(List.of())
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public OrderEntity updateOrderStatusReady(){
        List<OrderEntity> ordersInProcess = orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.IN_PROCESS);
        if(ordersInProcess.isEmpty()){
            ordersInProcess = orderRepository.findByOrderStatusOrderByInsertOrderDateAsc(OrderStatus.PENDING);
        }
        OrderEntity order = Optional.ofNullable(ordersInProcess)
                .orElseThrow(() -> new ResourceNotFoundException("There isn't any order to process!"))
                .stream()
                .findFirst()
                .get();

        if(order.getOrderStatus().equals(OrderStatus.IN_PROCESS)){
            order.setOrderStatus(OrderStatus.READY);
        }
        else{
            order.setOrderStatus(OrderStatus.IN_PROCESS);
        }
        return order;
    }

    private OrderDetailsProcessingResult processOrderDetails(List<OrderDetailRequestDto> orderDetailsRequestDto){

        AtomicInteger pizzaTotalQuantity = new AtomicInteger(0);
        DoubleAdder pizzaTotalPrice = new DoubleAdder();

        List<OrderDetailEntity> orderDetailsEntity =
                orderDetailsRequestDto.stream()
                        .map(orderDetailDto -> {
                            // Trova la pizza per ID. Se non esiste, lancia un'eccezione
                            PizzaEntity pizzaEntity = pizzaRepository.findById(orderDetailDto.getPizzaId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Pizza with ID " + orderDetailDto.getPizzaId() + " not found."));

                            // Calcola il prezzo per questo item e aggiungilo al totale
                            double pizzaDetailTotalPrice = pizzaEntity.getPrice() * orderDetailDto.getQuantity();

                            pizzaTotalQuantity.addAndGet(orderDetailDto.getQuantity());
                            pizzaTotalPrice.add(pizzaDetailTotalPrice);

                            // Crea l'OrderDetail
                            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                            orderDetailEntity.setPizza(pizzaEntity);
                            orderDetailEntity.setQuantity(orderDetailDto.getQuantity());
                            orderDetailEntity.setAdditionalInfo(orderDetailDto.getVariations());

                            // Non impostiamo qui l'Order, lo faremo dopo aver creato l'Order principale
                            return orderDetailEntity;
                        }).collect(Collectors.toList());
        return new OrderDetailsProcessingResult(orderDetailsEntity, pizzaTotalQuantity, pizzaTotalPrice);
    }
}
