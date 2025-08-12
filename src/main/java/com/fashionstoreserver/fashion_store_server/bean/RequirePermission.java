package com.fashionstoreserver.fashion_store_server.bean;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    String value(); // tên quyền yêu cầu
}