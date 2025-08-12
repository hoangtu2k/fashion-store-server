package com.fashionstoreserver.fashion_store_server.controller.admin;

import com.fashionstoreserver.fashion_store_server.bean.RequirePermission;
import com.fashionstoreserver.fashion_store_server.entity.User;
import com.fashionstoreserver.fashion_store_server.request.UserRequest;
import com.fashionstoreserver.fashion_store_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @RequirePermission("MANAGE_USERS")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public User createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{username}/roles/{roleName}")
    public void assignRole(@PathVariable String username, @PathVariable String roleName) {
        userService.assignRoleToUser(username, roleName);
    }
}
