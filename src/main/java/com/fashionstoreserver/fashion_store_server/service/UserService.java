package com.fashionstoreserver.fashion_store_server.service;

import com.fashionstoreserver.fashion_store_server.bean.JwtTokenUtil;
import com.fashionstoreserver.fashion_store_server.entity.Role;
import com.fashionstoreserver.fashion_store_server.entity.User;
import com.fashionstoreserver.fashion_store_server.repository.RoleRepository;
import com.fashionstoreserver.fashion_store_server.repository.UserRepository;
import com.fashionstoreserver.fashion_store_server.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public User login(String identifier) {
        return userRepository.findByIdentifierWithRoles(identifier);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // Nên mã hóa bằng BCrypt

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Assign role to user
    public void assignRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    // Kiểm tra quyền
    public boolean hasPermission(User user, String permissionName) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }

    public Optional<User> getUserFromToken(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username);
    }






}
