package com.fashionstoreserver.fashion_store_server.controller;

import com.fashionstoreserver.fashion_store_server.entity.Customer;
import com.fashionstoreserver.fashion_store_server.request.CustomerRegisterRequest;
import com.fashionstoreserver.fashion_store_server.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerRegisterRequest request) {
        try {
            Customer created = customerService.registerCustomer(request);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
