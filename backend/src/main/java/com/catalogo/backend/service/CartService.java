package com.catalogo.backend.service;

import com.catalogo.backend.dto.CartItem;
import com.catalogo.backend.entity.User;
import com.catalogo.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redis;
    private final ProductRepository productRepo; // per snapshot prezzo
    private static final String CART_USER = "cart:user:%s";
    private static final String CART_ANON = "cart:anon:%s";

    // Recupera userId dal contesto security, altrimenti null
    private String currentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            var user = (User) auth.getPrincipal();
            return String.valueOf(user.getId());
        }
        return null;
    }

    private String currentKey(String anonId) {
        String userId = currentUserId();
        return (userId != null) ? CART_USER.formatted(userId) : CART_ANON.formatted(anonId);
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> read(String key) {
        Object v = redis.opsForValue().get(key);
        return (v instanceof List<?> l) ? (List<CartItem>) l : List.of();
    }

    private void write(String key, List<CartItem> items) {
        redis.opsForValue().set(key, items);
    }

    public List<CartItem> getCart(String anonId) {
        return read(currentKey(anonId));
    }

    public void addItem(String anonId, Long productId, int qty) {
        String key = currentKey(anonId);
        List<CartItem> items = new ArrayList<>(read(key));
        int idx = -1;
        for (int i=0;i<items.size();i++) if (items.get(i).productId().equals(productId)) { idx = i; break; }
        if (idx >= 0) {
            var it = items.get(idx);
            items.set(idx, new CartItem(productId, it.quantity()+qty, it.unitPriceSnapshot()));
        } else {
            var p = productRepo.findById(productId).orElseThrow();
            items.add(new CartItem(productId, qty, p.getPrice())); // snapshot prezzo
        }
        write(key, items);
    }

    public void updateQty(String anonId, Long productId, int qty) {
        String key = currentKey(anonId);
        List<CartItem> items = new ArrayList<>(read(key));
        items.removeIf(i -> i.productId().equals(productId));
        if (qty > 0) {
            var p = productRepo.findById(productId).orElseThrow();
            items.add(new CartItem(productId, qty, p.getPrice()));
        }
        write(key, items);
    }

    public void clear(String anonId) {
        redis.delete(currentKey(anonId));
    }

    // Merge carrello anonimo → utente (da chiamare al login)
    public void mergeAnonIntoUser(String anonId, Long userId) {
        String anonKey = CART_ANON.formatted(anonId);
        String userKey = CART_USER.formatted(userId);
        List<CartItem> anon = read(anonKey);
        if (!anon.isEmpty()) {
            List<CartItem> user = new ArrayList<>(read(userKey));
            for (CartItem a : anon) {
                int idx = -1;
                for (int i=0;i<user.size();i++) if (user.get(i).productId().equals(a.productId())) { idx = i; break; }
                if (idx >= 0) {
                    var u = user.get(idx);
                    user.set(idx, new CartItem(a.productId(), u.quantity()+a.quantity(), u.unitPriceSnapshot()));
                } else {
                    user.add(a);
                }
            }
            write(userKey, user);
            redis.delete(anonKey);
        }
    }
}
