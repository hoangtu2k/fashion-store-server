package com.fashionstoreserver.fashion_store_server.repository;

import com.fashionstoreserver.fashion_store_server.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT C FROM Customer C WHERE C.username = :identifier OR C.email = :identifier OR C.phone = :identifier")
    Customer findByIdentifier(@Param("identifier") String identifier);

    List<Customer> findAllByOrderByCreatedAtDesc();



}
