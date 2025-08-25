package com.catalogo.backend.dto;

public record AuthResponse(String token, String role, String username) { }
