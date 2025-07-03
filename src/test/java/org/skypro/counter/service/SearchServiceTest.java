package org.skypro.counter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skypro.counter.model.search.Searchable;
import org.skypro.counter.model.search.SearchResult;
import org.skypro.counter.model.article.Article;
import org.skypro.counter.model.product.SimpleProduct;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private StorageService storageService;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        storageService = mock(StorageService.class);
        searchService = new SearchService(storageService);
    }



    @Test
    void search_whenStorageIsEmpty_returnsEmptyList() {
        when(storageService.getAllSearchables()).thenReturn(List.of());

        Collection<SearchResult> results = searchService.search("test");

        assertTrue(results.isEmpty(), "Результат поиска должен быть пустым, когда в хранилище нет объектов");
        verify(storageService).getAllSearchables();
    }
    @Test
    void search_whenNoMatchingObjects_returnsEmptyList() {
        SimpleProduct product1 = new SimpleProduct(UUID.randomUUID(), "Apple", 100);
        SimpleProduct product2 = new SimpleProduct(UUID.randomUUID(), "Banana", 200);

        when(storageService.getAllSearchables()).thenReturn(List.of(product1, product2));

        Collection<SearchResult> results = searchService.search("Orange");

        assertTrue(results.isEmpty());
    }

    @Test
    void search_whenMatchingObjectExists_returnsResult() {
        SimpleProduct product1 = new SimpleProduct(UUID.randomUUID(), "Apple", 100);
        SimpleProduct product2 = new SimpleProduct(UUID.randomUUID(), "Banana", 200);

        when(storageService.getAllSearchables()).thenReturn(List.of(product1, product2));

        Collection<SearchResult> results = searchService.search("Apple");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        SearchResult result = results.iterator().next();
        assertEquals("Apple", result.getName());
        assertEquals("PRODUCT", result.getContentType());
    }

    @Test
    void search_whenMultipleMatches_returnsAllMatches() {
        SimpleProduct product1 = new SimpleProduct(UUID.randomUUID(), "Apple", 100);
        SimpleProduct product2 = new SimpleProduct(UUID.randomUUID(), "Green Apple", 150);
        SimpleProduct product3 = new SimpleProduct(UUID.randomUUID(), "Banana", 200);

        when(storageService.getAllSearchables()).thenReturn(List.of(product1, product2, product3));

        Collection<SearchResult> results = searchService.search("Apple");

        assertEquals(2, results.size());
    }
}
