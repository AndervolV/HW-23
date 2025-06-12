package org.skypro.counter.service;

import org.skypro.counter.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {
    private final StorageService storageService;

    public ProductService(StorageService storageService) {
        this.storageService = storageService;
    }

    public int getProductPrice(UUID productId) {
        return storageService.getAllProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .map(Product::getPrice)
                .orElse(0);
    }
}