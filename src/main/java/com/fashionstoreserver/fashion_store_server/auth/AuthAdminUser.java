package com.fashionstoreserver.fashion_store_server.auth;

import com.fashionstoreserver.fashion_store_server.bean.JwtTokenUtil;
import com.fashionstoreserver.fashion_store_server.entity.Permission;
import com.fashionstoreserver.fashion_store_server.entity.Role;
import com.fashionstoreserver.fashion_store_server.entity.User;
import com.fashionstoreserver.fashion_store_server.enums.Status;
import com.fashionstoreserver.fashion_store_server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AuthAdminUser {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        User user = userService.login(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thông tin đăng nhập không hợp lệ!");
        }

        if (user.getStatus() != Status.ACTIVE) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không khả dụng!");
        }

        String token = jwtTokenUtil.generateToken(user.getUsername());

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "fullName", user.getFullName(), // ✅ thêm tên đầy đủ
                "roles", user.getRoles().stream().map(Role::getName).toList(),
                "permissions", user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(Permission::getName)
                        .distinct()
                        .toList()
        ));

        return ResponseEntity.ok(tokenMap);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getByToken(@RequestParam("token") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.login(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("fullName", user.getFullName()); // ✅ thêm tên đầy đủ
        data.put("roles", user.getRoles().stream().map(Role::getName).toList());

        return ResponseEntity.ok(data);
    }



    @GetMapping("/me")
    public ResponseEntity<?> getCurrentAdmin(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }

        String token = authHeader.substring(7); // cắt "Bearer "
        String username = jwtTokenUtil.getUsernameFromToken(token);

        User user = userService.login(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("username", user.getUsername());
        userMap.put("fullName", user.getFullName()); // thêm nếu có
        userMap.put("roles", user.getRoles().stream().map(Role::getName).toList());
        userMap.put("permissions", user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .toList());

        return ResponseEntity.ok(userMap);
    }



}
