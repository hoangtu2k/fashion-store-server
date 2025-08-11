package com.fashionstoreserver.fashion_store_server.repository;

import com.fashionstoreserver.fashion_store_server.entity.Cart;
import com.fashionstoreserver.fashion_store_server.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomer(Customer customer);
}
