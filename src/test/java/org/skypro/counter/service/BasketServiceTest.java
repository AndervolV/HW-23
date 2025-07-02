package org.skypro.counter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.counter.exception.NoSuchProductException;
import org.skypro.counter.model.basket.BasketItem;
import org.skypro.counter.model.basket.UserBasket;
import org.skypro.counter.model.product.Product;
import org.skypro.counter.model.basket.ProductBasket;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketServiceTest {

    private ProductBasket productBasket;
    private StorageService storageService;
    private BasketService basketService;

    @BeforeEach
    void setUp() {
        productBasket = mock(ProductBasket.class);
        storageService = mock(StorageService.class);
        basketService = new BasketService(productBasket, storageService);
    }

    @Test
    void addProductToBasket_nonExistingProduct_throwsException() {
        UUID fakeId = UUID.randomUUID();
        when(storageService.getProductById(fakeId)).thenReturn(Optional.empty());

        NoSuchProductException exception = assertThrows(NoSuchProductException.class,
                () -> basketService.addProductToBasket(fakeId));

        assertTrue(exception.getMessage().contains("не найден"));
        verify(storageService).getProductById(fakeId);
        verifyNoMoreInteractions(productBasket);
    }

    @Test
    void addProductToBasket_existingProduct_callsAddProduct() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        basketService.addProductToBasket(productId);

        verify(storageService).getProductById(productId);
        verify(productBasket).addProduct(productId);
    }

    @Test
    void getUserBasket_emptyBasket_returnsEmptyUserBasket() {
        when(productBasket.getBasketItems()).thenReturn(Collections.emptyMap());

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertTrue(userBasket.getItems().isEmpty());
        assertEquals(0, userBasket.getTotal());
        verify(productBasket).getBasketItems();
        verifyNoMoreInteractions(storageService);
    }

    @Test
    void getUserBasket_withItems_returnsUserBasketWithItems() {
        UUID productId = UUID.randomUUID();
        int quantity = 3;
        Map<UUID, Integer> basketItemsMap = new HashMap<>();
        basketItemsMap.put(productId, quantity);

        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(100);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));
        when(productBasket.getBasketItems()).thenReturn(basketItemsMap);

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertEquals(1, userBasket.getItems().size());
        BasketItem item = userBasket.getItems().get(0);
        assertEquals(quantity, item.getQuantity());
        assertEquals(product, item.getProduct());
        assertEquals(100 * quantity, userBasket.getTotal());

        verify(productBasket).getBasketItems();
        verify(storageService).getProductById(productId);
    }
}
