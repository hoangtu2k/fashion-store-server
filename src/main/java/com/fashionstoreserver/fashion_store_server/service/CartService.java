package com.fashionstoreserver.fashion_store_server.service;

import com.fashionstoreserver.fashion_store_server.entity.*;
import com.fashionstoreserver.fashion_store_server.repository.CartItemRepository;
import com.fashionstoreserver.fashion_store_server.repository.CartRepository;
import com.fashionstoreserver.fashion_store_server.repository.CustomerRepository;
import com.fashionstoreserver.fashion_store_server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Cart getOrCreateCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .customer(customer)
                            .totalPrice(BigDecimal.ZERO)  // Khởi tạo bằng BigDecimal.ZERO
                            .build();
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public Cart addItemToCart(Long customerId, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(customerId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .price(product.getPrice())
                .build();

        cartItemRepository.save(cartItem);

        // Cập nhật tổng tiền: cart.totalPrice = cart.totalPrice + product.price * quantity
        BigDecimal currentTotal = cart.getTotalPrice() != null ? cart.getTotalPrice() : BigDecimal.ZERO;
        BigDecimal addition = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        cart.setTotalPrice(currentTotal.add(addition));

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItemFromCart(Long customerId, Long cartItemId) {
        Cart cart = getOrCreateCart(customerId);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        BigDecimal currentTotal = cart.getTotalPrice() != null ? cart.getTotalPrice() : BigDecimal.ZERO;
        BigDecimal subtraction = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        cart.setTotalPrice(currentTotal.subtract(subtraction));

        cartItemRepository.delete(item);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart clearCart(Long customerId) {
        Cart cart = getOrCreateCart(customerId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.setTotalPrice(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }
}
