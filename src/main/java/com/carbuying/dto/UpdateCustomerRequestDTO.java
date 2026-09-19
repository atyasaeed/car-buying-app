package com.carbuying.dto;

import com.carbuying.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCustomerRequestDTO {
    @NotNull(message = "status is required")
    private RequestStatus status;
}
