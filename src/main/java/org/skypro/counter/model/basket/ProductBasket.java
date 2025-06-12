package org.skypro.counter.model.basket;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProductBasket {
    private final Map<UUID, Integer> basketItems = new HashMap<>();

    public void addProduct(UUID productId) {
        basketItems.merge(productId, 1, Integer::sum);
    }

    public Map<UUID, Integer> getBasketItems() {
        return Collections.unmodifiableMap(basketItems);
    }

    public void clear() {
        basketItems.clear();
    }

    public int getTotalItemCount() {
        return basketItems.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}