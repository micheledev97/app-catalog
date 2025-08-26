package com.catalogo.backend.dto;

import java.io.Serializable;
import java.math.BigDecimal;
public record ProductDTO(Long id, String name, String description, String category, BigDecimal price) implements Serializable {}
