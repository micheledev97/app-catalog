package com.catalogo.backend.controller;

import com.catalogo.backend.dto.ProductDTO;
import com.catalogo.backend.dto.ProductFilter;
import com.catalogo.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService svc;

    @GetMapping
    public Page<ProductDTO> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "sort", required = false) String sort
    ){
        ProductFilter f = new ProductFilter(name, category, minPrice, maxPrice);
        return svc.list(page, size, sort, f);
    }

    @GetMapping("/{id}")
    public ProductDTO get(@PathVariable("id") Long id) {
        return svc.get(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO dto){
        return ResponseEntity.ok(svc.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> update(@PathVariable("id") Long id, @Valid @RequestBody ProductDTO dto){
        return ResponseEntity.ok(svc.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
