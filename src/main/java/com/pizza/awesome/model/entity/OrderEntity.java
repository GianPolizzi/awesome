package com.pizza.awesome.model.entity;

import com.pizza.awesome.utils.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "AW_ORDER")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_address", nullable = false)
    private String customerAddress;

    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "insert_order_date", nullable = false)
    private LocalDateTime insertOrderDate;

    @Column(name = "update_order_date")
    private LocalDateTime updateOrderDate;

    // --- Relazione Uno-a-Molti con OrderDetail ---
    // mappedBy indica il campo nell'entità OrderDetail che "gestisce" la relazione
    // cascade = CascadeType.ALL significa che se elimini un Order, vengono eliminati anche i suoi OrderDetails
    // orphanRemoval = true significa che se rimuovi un OrderDetail dalla lista, viene eliminato dal DB
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();
}
