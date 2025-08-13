package com.fashionstoreserver.fashion_store_server.bean;

import com.fashionstoreserver.fashion_store_server.entity.User;
import com.fashionstoreserver.fashion_store_server.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Lấy role name làm authority
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // hoặc tùy logic của bạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // hoặc tùy logic của bạn
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // hoặc tùy logic của bạn
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == Status.ACTIVE; // Ví dụ nếu có trường status
    }
}
