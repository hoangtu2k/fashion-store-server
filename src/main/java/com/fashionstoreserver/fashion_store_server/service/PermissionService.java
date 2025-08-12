package com.fashionstoreserver.fashion_store_server.service;

import com.fashionstoreserver.fashion_store_server.entity.Permission;
import com.fashionstoreserver.fashion_store_server.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }
}