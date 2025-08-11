package com.fashionstoreserver.fashion_store_server.service;

import com.fashionstoreserver.fashion_store_server.entity.Customer;
import com.fashionstoreserver.fashion_store_server.enums.Status;
import com.fashionstoreserver.fashion_store_server.repository.CustomerRepository;
import com.fashionstoreserver.fashion_store_server.request.CustomerRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Customer login(String identifier) {
        return customerRepository.findByIdentifier(identifier);
    }

    public Customer registerCustomer(CustomerRegisterRequest request) {
        // Kiểm tra username hoặc email đã tồn tại chưa
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Customer customer = Customer.builder()
                .code("KH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .status(Status.ACTIVE)
                .build();

        return customerRepository.save(customer);
    }
}
