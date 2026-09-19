package com.carbuying.entity;

import com.carbuying.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_offers", indexes = {
    @Index(name = "idx_supplier_id", columnList = "supplier_id"),
    @Index(name = "idx_request_id", columnList = "customer_request_id"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long supplierId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_request_id", nullable = false)
    private CustomerRequest customerRequest;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @Column(nullable = false)
    private Integer inspectionScore;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String carDetails;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
