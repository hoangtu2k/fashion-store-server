package com.fashionstoreserver.fashion_store_server.repository;

import com.fashionstoreserver.fashion_store_server.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
