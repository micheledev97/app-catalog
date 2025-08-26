package com.catalogo.backend.dto;

import java.math.BigDecimal;

public record CartItem(Long productId, int quantity, BigDecimal unitPriceSnapshot) {}
