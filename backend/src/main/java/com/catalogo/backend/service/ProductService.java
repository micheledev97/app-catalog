package com.catalogo.backend.service;


import com.catalogo.backend.dto.ProductDTO;
import com.catalogo.backend.dto.ProductFilter;
import com.catalogo.backend.entity.Product;
import com.catalogo.backend.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public static ProductDTO toDTO(Product p){
        return new ProductDTO(p.getId(), p.getName(), p.getDescription(), p.getCategory(), p.getPrice());
    }

    private Specification<Product> spec(ProductFilter f) {
        Specification<Product> s = Specification.where(null);

        if (f.name() != null && !f.name().isBlank()) {
            s = s.and((root, q, cb) -> cb.like(
                    cb.lower(root.get("name").as(String.class)),
                    "%" + f.name().toLowerCase() + "%"));
        }
        if (f.category() != null && !f.category().isBlank()) {
            s = s.and((root, q, cb) -> cb.like(
                    cb.lower(root.get("category").as(String.class)),
                    "%" + f.category().toLowerCase() + "%"));
        }
        if (f.minPrice() != null || f.maxPrice() != null) {
            s = s.and((root, q, cb) -> {
                var price = root.get("price").as(BigDecimal.class);
                if (f.minPrice() != null && f.maxPrice() != null) return cb.between(price, f.minPrice(), f.maxPrice());
                if (f.minPrice() != null) return cb.greaterThanOrEqualTo(price, f.minPrice());
                return cb.lessThanOrEqualTo(price, f.maxPrice());
            });
        }
        return s;
    }


    @Cacheable(
            value = "product_list",
            key = "T(java.util.Objects).hash("
                    + "#page, #size, "
                    + "(#sort?:''), "
                    + "(#filter?.name?:''), "
                    + "(#filter?.category?:''), "
                    + "(#filter?.minPrice?:''), "
                    + "(#filter?.maxPrice?:'')"
                    + ")"
    )
    public Page<ProductDTO> list(int page, int size, String sort, ProductFilter filter) {
        Sort s = (sort == null || sort.isBlank()) ? Sort.by("id").descending() : Sort.by(sort);
        Page<Product> res = repo.findAll(spec(filter), PageRequest.of(page, size, s));
        return res.map(ProductService::toDTO);
    }

    @Cacheable(value="product_detail", key="#id")
    public ProductDTO get(Long id) {
        Product p = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return toDTO(p);
    }

    @CacheEvict(value={"product_list","product_detail"}, allEntries = true)
    public ProductDTO create(ProductDTO dto) {
        Product p = Product.builder()
                .name(dto.name()).description(dto.description())
                .category(dto.category()).price(dto.price()).build();
        return toDTO(repo.save(p));
    }

    @CacheEvict(value={"product_list","product_detail"}, allEntries = true)
    public ProductDTO update(Long id, ProductDTO dto) {
        Product p = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        p.setName(dto.name()); p.setDescription(dto.description());
        p.setCategory(dto.category()); p.setPrice(dto.price());
        return toDTO(repo.save(p));
    }

    @CacheEvict(value={"product_list","product_detail"}, allEntries = true)
    public void delete(Long id) { repo.deleteById(id); }

    @PostConstruct
    void seed() {
        if (repo.count() == 0) {
            repo.saveAll(List.of(
                    Product.builder().name("Laptop Pro 14").description("Ultrabook").category("Informatica").price(new BigDecimal("1499.99")).build(),
                    Product.builder().name("Mouse Wireless").description("2.4GHz").category("Accessori").price(new BigDecimal("24.90")).build(),
                    Product.builder().name("Monitor 27\" 4K").description("IPS HDR").category("Periferiche").price(new BigDecimal("349.00")).build()
            ));
        }
    }
}
