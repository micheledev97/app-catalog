package com.catalogo.backend.controller;

import com.catalogo.backend.dto.AuthResponse;
import com.catalogo.backend.dto.LoginRequest;
import com.catalogo.backend.dto.RegisterRequest;
import com.catalogo.backend.entity.Role;
import com.catalogo.backend.entity.User;
import com.catalogo.backend.security.JwtUtil;
import com.catalogo.backend.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwt;
    private final UserService users;

    @PostConstruct
    void init(){ users.ensureAdminExists(); }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        User u = users.loadUserByUsername(req.username());
        String token = jwt.generateToken(u.getUsername(), u.getRole());
        return ResponseEntity.ok(new AuthResponse(token, u.getRole().name(), u.getUsername()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        User u = users.register(req);
        String token = jwt.generateToken(u.getUsername(), Role.USER);
        return ResponseEntity.ok(new AuthResponse(token, u.getRole().name(), u.getUsername()));
    }

        @GetMapping("/me")
        public Map<String, Object> me(@org.springframework.security.core.annotation.AuthenticationPrincipal com.catalogo.backend.entity.User u) {
            if (u == null) return Map.of("authenticated", false);
            return Map.of(
                    "authenticated", true,
                    "username", u.getUsername(),
                    "role", u.getRole().name(),
                    "authorities", u.getAuthorities().stream().map(Object::toString).toList()
            );
        }
    }

