package org.skypro.counter.controller;

import org.skypro.counter.model.basket.UserBasket;
import org.skypro.counter.service.BasketService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/{id}")
    public String addProduct(@PathVariable("id") UUID id) {
        basketService.addProductToBasket(id);
        return "Продукт успешно добавлен";
    }

    @GetMapping
    public UserBasket getUserBasket() {
        return basketService.getUserBasket();
    }
}