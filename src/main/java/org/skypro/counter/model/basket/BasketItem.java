package org.skypro.counter.model.basket;

import org.skypro.counter.model.product.Product;

import java.util.Objects;

public final class BasketItem {
    private final Product product;
    private final int quantity;

    public BasketItem(Product product, int quantity) {
        this.product = Objects.requireNonNull(product, "Продукт не может быть null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным числом");
        }
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketItem that = (BasketItem) o;
        return quantity == that.quantity && product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity);
    }
}