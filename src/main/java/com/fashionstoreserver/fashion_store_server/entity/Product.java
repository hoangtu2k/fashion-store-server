package com.fashionstoreserver.fashion_store_server.entity;

import com.fashionstoreserver.fashion_store_server.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã nội bộ cửa hàng (ví dụ: SP0001)
    @NotNull
    @Column(nullable = false, unique = true)
    private String code;

    // Mã vạch sản phẩm (EAN-13 hoặc khác)
    @NotNull
    @Column(nullable = false, unique = true)
    private String barcode;

    @NotNull
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    private Integer stockQuantity;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Nếu không nhập code, tự sinh
        if (this.code == null || this.code.trim().isEmpty()) {
            this.code = "SP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }



}
