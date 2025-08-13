package com.fashionstoreserver.fashion_store_server.auth;

import com.fashionstoreserver.fashion_store_server.entity.Customer;
import com.fashionstoreserver.fashion_store_server.enums.Status;
import com.fashionstoreserver.fashion_store_server.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/customers")
public class AuthCustomer {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomerService service;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {

        Customer customer = service.login(loginRequest.getUsername());

        if (customer == null || !passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thông tin đăng nhập không hợp lệ!");
        }

        if (customer.getStatus() != Status.ACTIVE) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không khả dụng!");
        }

        String token = jwtTokenUtil.generateToken(loginRequest.getUsername());

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("user", Map.of(
                "id", customer.getId(),
                "username", customer.getUsername(),
                "fullName", customer.getFullName(),
                "phone", customer.getPhone(),
                "email", customer.getEmail()

        ));

        return ResponseEntity.ok(tokenMap);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getByToken(@RequestParam("token") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        Customer customer = service.login(username);

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", customer.getId());
        data.put("username", customer.getUsername());
        data.put("fullName", customer.getFullName());
        data.put("email", customer.getEmail());
        data.put("phone", customer.getPhone());

        return ResponseEntity.ok(data);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCustomerByToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }

        String token = authHeader.substring(7); // cắt "Bearer "
        String username = jwtTokenUtil.getUsernameFromToken(token);

        Customer customer = service.login(username);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer not found");
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", customer.getId());
        userMap.put("username", customer.getUsername());
        userMap.put("fullName", customer.getFullName());
        userMap.put("email", customer.getEmail());
        userMap.put("phone", customer.getPhone());

        return ResponseEntity.ok(userMap);
    }


}