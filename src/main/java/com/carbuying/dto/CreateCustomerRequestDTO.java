package com.carbuying.dto;

import com.carbuying.enums.InspectionCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequestDTO {
    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "checkedByCompany is required")
    private InspectionCompany checkedByCompany;
}
