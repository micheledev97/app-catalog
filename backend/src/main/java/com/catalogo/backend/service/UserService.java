package com.catalogo.backend.service;

import com.catalogo.backend.dto.RegisterRequest;
import com.catalogo.backend.entity.Role;
import com.catalogo.backend.entity.User;
import com.catalogo.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public User loadUserByUsername(String username) {
        return repo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User register(RegisterRequest req) {
        if (repo.existsByUsername(req.username())) throw new RuntimeException("Username already exists");
        if (repo.existsByEmail(req.email())) throw new RuntimeException("Email already exists");
        User u = User.builder()
                .username(req.username())
                .email(req.email())
                .password(encoder.encode(req.password()))
                .role(Role.USER)
                .build();
        return repo.save(u);
    }

    public List<User> getAllUser(){
        return repo.findAll();
    }
    public void ensureAdminExists() {
        if (!repo.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(encoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            repo.save(admin);
        }
    }
}
