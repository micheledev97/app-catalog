package com.catalogo.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) private Order order;
    @ManyToOne(optional=false) private Product product;

    @Column(nullable=false) private int quantity;
    @Column(nullable=false, precision=12, scale=2) private BigDecimal price; // unit price at purchase time
}
