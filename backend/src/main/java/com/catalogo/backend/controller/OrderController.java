package com.catalogo.backend.controller;

import com.catalogo.backend.dto.OrderRequest;
import com.catalogo.backend.dto.OrderResponse;
import com.catalogo.backend.entity.User;
import com.catalogo.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService svc;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@AuthenticationPrincipal User user, @RequestBody OrderRequest req){
        return ResponseEntity.ok(svc.create(user, req));
    }

    @GetMapping
    public Page<OrderResponse> myOrders(@AuthenticationPrincipal User user,
                                        @RequestParam(defaultValue="0") int page,
                                        @RequestParam(defaultValue="10") int size) {
        return svc.myOrders(user, page, size);
    }
}
