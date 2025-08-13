package com.fashionstoreserver.fashion_store_server.request;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {
    private String username;
    private String password;
    private Set<Long> roleIds;
    private Set<Long> permissionIds;
}
