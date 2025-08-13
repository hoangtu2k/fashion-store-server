package com.fashionstoreserver.fashion_store_server.bean;

import com.fashionstoreserver.fashion_store_server.entity.User;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;

@Aspect
@Component
public class RoleAspect {

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (!(principal instanceof MyUserDetails)) {
            throw new RuntimeException("User không hợp lệ");
        }

        MyUserDetails userDetails = (MyUserDetails) principal;
        User user = userDetails.getUser();

        boolean hasRole = user.getRoles().stream()
                .map(role -> role.getName())
                .anyMatch(r -> {
                    for (String neededRole : requireRole.value()) {
                        if (r.equals(neededRole)) return true;
                    }
                    return false;
                });

        if (!hasRole) {
            throw new RuntimeException("Không có quyền truy cập");
        }
    }
}
