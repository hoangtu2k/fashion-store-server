package com.fashionstoreserver.fashion_store_server.repository;

import com.fashionstoreserver.fashion_store_server.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
