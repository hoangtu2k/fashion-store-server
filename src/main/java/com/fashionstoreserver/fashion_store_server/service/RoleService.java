package com.fashionstoreserver.fashion_store_server.service;

import com.fashionstoreserver.fashion_store_server.entity.Permission;
import com.fashionstoreserver.fashion_store_server.entity.Role;
import com.fashionstoreserver.fashion_store_server.repository.PermissionRepository;
import com.fashionstoreserver.fashion_store_server.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role assignPermission(String roleName, String permissionName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        role.getPermissions().add(permission);
        return roleRepository.save(role);
    }
}
