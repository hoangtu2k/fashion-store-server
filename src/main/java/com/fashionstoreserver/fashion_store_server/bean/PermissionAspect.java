package com.fashionstoreserver.fashion_store_server.bean;

import com.fashionstoreserver.fashion_store_server.bean.RequirePermission;
import com.fashionstoreserver.fashion_store_server.entity.User;
import com.fashionstoreserver.fashion_store_server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    private final UserService userService;
    private final HttpServletRequest request;

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Lấy user từ token
        Optional<User> userOpt = userService.getUserFromToken(token);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>("Không xác thực được người dùng", HttpStatus.UNAUTHORIZED);
        }
        User user = userOpt.get();


        // Kiểm tra quyền
        String requiredPerm = requirePermission.value();
        boolean hasPermission = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(perm -> perm.getName().equals(requiredPerm));

        if (!hasPermission) {
            return new ResponseEntity<>("Không đủ quyền truy cập", HttpStatus.FORBIDDEN);
        }

        // Nếu đủ quyền thì tiếp tục
        return joinPoint.proceed();
    }
}
