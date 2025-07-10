package com.pizza.awesome.service;

import com.pizza.awesome.exception.ResourceNotFoundException;
import com.pizza.awesome.model.dto.OrderRequestDto;
import com.pizza.awesome.model.entity.OrderDetailEntity;
import com.pizza.awesome.model.entity.OrderEntity;
import com.pizza.awesome.model.entity.PizzaEntity;
import com.pizza.awesome.repository.OrderRepository;
import com.pizza.awesome.repository.PizzaRepository;
import com.pizza.awesome.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    public OrderEntity createOrder(OrderRequestDto orderRequestDto){

        AtomicInteger pizzaTotalQuantity = new AtomicInteger(0);
        DoubleAdder pizzaTotalPrice = new DoubleAdder();

        List<OrderDetailEntity> orderDetailsEntity =
                orderRequestDto.getOrderDetailsDto().stream()
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

        // 2. Creazione dell'Entità Order
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerName(orderRequestDto.getCustomerName());
        orderEntity.setCustomerAddress(orderRequestDto.getCustomerAddress());
        orderEntity.setCustomerPhone(orderRequestDto.getCustomerPhone());
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        orderEntity.setInsertOrderDate(LocalDateTime.now());
        orderEntity.setTotalQuantity(pizzaTotalQuantity.get());
        orderEntity.setTotalPrice(pizzaTotalPrice.sum());

        // 3. Collega i Dettagli all'Ordine e viceversa
        // Imposta la relazione bidirezionale Order <-> OrderDetail
        orderDetailsEntity.forEach(orderDetailEntity -> orderDetailEntity.setOrder(orderEntity)); // Imposta l'Order in ogni OrderDetail
        orderEntity.setOrderDetails(orderDetailsEntity); // Imposta la lista di OrderDetails nell'Order

        // 4. Salva l'Ordine (che casca anche i dettagli)
        return orderRepository.save(orderEntity);
    }
}
