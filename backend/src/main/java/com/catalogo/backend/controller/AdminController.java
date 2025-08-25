package com.catalogo.backend.controller;

import com.catalogo.backend.dto.UserSummary;
import com.catalogo.backend.entity.User;
import com.catalogo.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService users;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // equivalente a hasRole('ADMIN'), ma esplicito
    public ResponseEntity<List<UserSummary>> allUsers() {
        List<User> list = users.getAllUser();
        List<UserSummary> out = list.stream()
                .map(u -> new UserSummary(u.getId(), u.getUsername(), u.getEmail(), u.getRole().name()))
                .toList();
        return ResponseEntity.ok(out);
    }
}
