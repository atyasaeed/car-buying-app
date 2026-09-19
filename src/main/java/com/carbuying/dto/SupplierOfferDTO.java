package com.carbuying.dto;

import com.carbuying.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierOfferDTO {
    private Long id;
    private Long supplierId;
    private Long customerRequestId;
    private OfferStatus status;
    private Integer inspectionScore;
    private String carDetails;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
