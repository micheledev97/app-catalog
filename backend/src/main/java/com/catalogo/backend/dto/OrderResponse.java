package com.catalogo.backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id, String status, BigDecimal total, Instant createdAt,
        List<Item> items
) {
    public record Item(Long productId, String name, int quantity, BigDecimal price) { }
}
