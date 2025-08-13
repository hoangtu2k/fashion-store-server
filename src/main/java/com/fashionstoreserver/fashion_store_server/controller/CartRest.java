package com.fashionstoreserver.fashion_store_server.controller;

import com.fashionstoreserver.fashion_store_server.entity.Cart;
import com.fashionstoreserver.fashion_store_server.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRest {

    private final CartService cartService;

    @GetMapping("/{customerId}")
    public Cart getCart(@PathVariable Long customerId) {
        return cartService.getOrCreateCart(customerId);
    }

    @PostMapping("/{customerId}/add")
    public Cart addItem(
            @PathVariable Long customerId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return cartService.addItemToCart(customerId, productId, quantity);
    }

    @DeleteMapping("/{customerId}/remove/{cartItemId}")
    public Cart removeItem(
            @PathVariable Long customerId,
            @PathVariable Long cartItemId) {
        return cartService.removeItemFromCart(customerId, cartItemId);
    }

    @DeleteMapping("/{customerId}/clear")
    public Cart clearCart(@PathVariable Long customerId) {
        return cartService.clearCart(customerId);
    }
}
