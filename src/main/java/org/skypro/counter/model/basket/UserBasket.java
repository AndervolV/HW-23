package org.skypro.counter.model.basket;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class UserBasket {
    private final List<BasketItem> items;
    private final int total;

    public UserBasket(List<BasketItem> items) {
        this.items = Collections.unmodifiableList(Objects.requireNonNull(items));
        this.total = calculateTotal();
    }

    private int calculateTotal() {
        return items.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }
}