package com.catalogo.backend.dto;

import java.math.BigDecimal;
public record ProductFilter(
        String name, String category, BigDecimal minPrice, BigDecimal maxPrice
) {}
