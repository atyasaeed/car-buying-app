package com.carbuying.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSupplierOfferDTO {
    @NotNull(message = "supplierId is required")
    private Long supplierId;

    @NotBlank(message = "carDetails is required")
    private String carDetails;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.01", message = "price must be greater than 0")
    private BigDecimal price;
}
