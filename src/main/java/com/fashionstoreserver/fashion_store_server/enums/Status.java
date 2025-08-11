package com.fashionstoreserver.fashion_store_server.enums;

public enum Status {
    // Trạng thái chung
    ACTIVE,        // Đang hoạt động
    INACTIVE,      // Ngừng hoạt động / Tạm khóa

    // Trạng thái sản phẩm
    IN_STOCK,      // Còn hàng
    OUT_OF_STOCK,  // Hết hàng
    DISCONTINUED,  // Ngừng kinh doanh

    // Trạng thái đơn hàng
    PENDING,       // Chờ xử lý
    PROCESSING,    // Đang xử lý
    SHIPPED,       // Đã giao cho đơn vị vận chuyển
    DELIVERED,     // Đã giao hàng
    CANCELLED,     // Đã hủy
    RETURNED,      // Đã trả hàng
    REFUNDED,      // Đã hoàn tiền

    // Trạng thái thanh toán
    UNPAID,        // Chưa thanh toán
    PAID,          // Đã thanh toán
    PARTIALLY_PAID // Thanh toán một phần
}

