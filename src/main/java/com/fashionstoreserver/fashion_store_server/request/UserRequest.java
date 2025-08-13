package com.fashionstoreserver.fashion_store_server.request;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {
    private String fullName;
    private String email;
    private String phone;
    private String username;
    private String code;
    private String status; // enum dưới dạng String (ví dụ: "ACTIVE", "INACTIVE")
    private Set<Long> roleIds;
}
