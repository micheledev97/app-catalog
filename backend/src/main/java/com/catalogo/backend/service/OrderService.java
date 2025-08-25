package com.catalogo.backend.service;

import com.catalogo.backend.dto.OrderRequest;
import com.catalogo.backend.dto.OrderResponse;
import com.catalogo.backend.entity.Order;
import com.catalogo.backend.entity.OrderItem;
import com.catalogo.backend.entity.Product;
import com.catalogo.backend.entity.User;
import com.catalogo.backend.repository.OrderRepository;
import com.catalogo.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    public OrderResponse create(User user, OrderRequest req) {
        Order o = Order.builder().user(user).status("NEW").total(BigDecimal.ZERO).build();

        var items = req.items().stream().map(i -> {
            Product p = productRepo.findById(i.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + i.productId()));
            return OrderItem.builder()
                    .order(o).product(p)
                    .quantity(i.quantity())
                    .price(p.getPrice()).build();
        }).toList();

        o.setItems(items);
        BigDecimal total = items.stream()
                .map(it -> it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        o.setTotal(total);

        // pagamento fittizio
        o.setStatus("PAID");
        Order saved = orderRepo.save(o);
        return toResponse(saved);
    }

    public Page<OrderResponse> myOrders(User user, int page, int size) {
        return orderRepo.findByUser(user, PageRequest.of(page,size)).map(this::toResponse);
    }

    private OrderResponse toResponse(Order o) {
        return new OrderResponse(
                o.getId(), o.getStatus(), o.getTotal(), o.getCreatedAt(),
                o.getItems().stream()
                        .map(it -> new OrderResponse.Item(
                                it.getProduct().getId(),
                                it.getProduct().getName(),
                                it.getQuantity(),
                                it.getPrice()))
                        .collect(Collectors.toList())
        );
    }
}
