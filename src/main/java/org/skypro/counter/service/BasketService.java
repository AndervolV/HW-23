package org.skypro.counter.service;

import org.skypro.counter.model.basket.BasketItem;
import org.skypro.counter.model.basket.ProductBasket;
import org.skypro.counter.model.basket.UserBasket;
import org.skypro.counter.model.product.Product;
import org.springframework.stereotype.Service;
import org.skypro.counter.exception.NoSuchProductException;

import java.util.*;

@Service
public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    public void addProductToBasket(UUID productId) {
        Optional<Product> productOpt = storageService.getProductById(productId);
        if (productOpt.isEmpty()) {
            throw new NoSuchProductException("Продукт с ID " + productId + " не найден");
        }
        productBasket.addProduct(productId);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketItems = productBasket.getBasketItems();
        List<BasketItem> items = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : basketItems.entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();

            storageService.getProductById(productId)
                    .ifPresent(product ->
                            items.add(new BasketItem(product, quantity)));
        }

        return new UserBasket(items);
    }
}