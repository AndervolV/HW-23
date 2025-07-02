
package org.skypro.counter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skypro.counter.model.search.Searchable;
import org.skypro.counter.model.search.SearchResult;

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

    private Searchable createSearchable(String name, String contentType, String searchTerm) {
        return new Searchable() {
            private final UUID id = UUID.randomUUID();

            @Override
            public String getSearchTerm() {
                return searchTerm;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public UUID getId() {
                return id;
            }
        };
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
        Searchable s1 = createSearchable("Apple", "PRODUCT", "Apple iPhone");
        Searchable s2 = createSearchable("Banana", "PRODUCT", "Yellow banana");

        when(storageService.getAllSearchables()).thenReturn(List.of(s1, s2));

        Collection<SearchResult> results = searchService.search("Orange");

        assertTrue(results.isEmpty(), "Результат поиска должен быть пустым, если нет подходящих объектов");
    }

    @Test
    void search_whenMatchingObjectExists_returnsResult() {
        Searchable s1 = createSearchable("Apple", "PRODUCT", "Apple iPhone");
        Searchable s2 = createSearchable("Banana", "PRODUCT", "Yellow banana");

        when(storageService.getAllSearchables()).thenReturn(List.of(s1, s2));

        Collection<SearchResult> results = searchService.search("Apple");

        assertFalse(results.isEmpty(), "Результат поиска не должен быть пустым, если есть подходящие объекты");
        assertEquals(1, results.size(), "Должен быть один результат");
        SearchResult result = results.iterator().next();
        assertEquals("Apple", result.getName());
        assertEquals("PRODUCT", result.getContentType());
    }

    @Test
    void search_whenMultipleMatches_returnsAllMatches() {
        Searchable s1 = createSearchable("Apple", "PRODUCT", "Apple iPhone");
        Searchable s2 = createSearchable("Green Apple", "PRODUCT", "Green Apple");
        Searchable s3 = createSearchable("Banana", "PRODUCT", "Yellow banana");

        when(storageService.getAllSearchables()).thenReturn(List.of(s1, s2, s3));

        Collection<SearchResult> results = searchService.search("Apple");

        assertEquals(2, results.size(), "Должны быть два результата по поиску 'Apple'");
    }
}
