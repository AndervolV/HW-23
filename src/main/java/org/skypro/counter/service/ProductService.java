package org.skypro.counter.service;

import org.skypro.counter.model.product.Product;
import org.springframework.stereotype.Service;
import org.skypro.counter.exception.NoSuchProductException;
import java.util.UUID;

@Service
public class ProductService {
    private final StorageService storageService;

    public ProductService(StorageService storageService) {
        this.storageService = storageService;
    }

    public int getProductPrice(UUID productId) {
        return storageService.getProductById(productId)
                .orElseThrow(() -> new NoSuchProductException("Продукт с ID " + productId + " не найден"))
                .getPrice();
    }
}