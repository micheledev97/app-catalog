package com.catalogo.backend.dto;

import com.catalogo.backend.dto.OrderItemRequest;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
public record OrderRequest(@NotEmpty List<OrderItemRequest> items) {}
