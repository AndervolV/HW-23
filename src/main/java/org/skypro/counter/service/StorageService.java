package org.skypro.counter.service;

import org.skypro.counter.model.article.Article;
import org.skypro.counter.model.product.Product;
import org.skypro.counter.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> products = new HashMap<>();
    private final Map<UUID, Article> articles = new HashMap<>();

    public StorageService() {
        addTestProducts();
        addTestArticles();
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> result = new ArrayList<>();
        result.addAll(products.values());
        result.addAll(articles.values());
        return result;
    }

    private void addTestProducts() {
        UUID productId1 = UUID.randomUUID();
        products.put(productId1, new Product(productId1, "Ноутбук") {
            @Override
            public int getPrice() {
                return 50000;
            }
        });

        UUID productId2 = UUID.randomUUID();
        products.put(productId2, new Product(productId2, "Телефон") {
            @Override
            public int getPrice() {
                return 30000;
            }
        });
    }

    private void addTestArticles() {
        articles.put(UUID.randomUUID(),
                new Article(UUID.randomUUID(), "Новости", "Сегодня хорошая погода"));
        articles.put(UUID.randomUUID(),
                new Article(UUID.randomUUID(), "Советы", "Как научиться программировать"));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public List<Article> getAllArticles() {
        return new ArrayList<>(articles.values());
    }
    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }
}