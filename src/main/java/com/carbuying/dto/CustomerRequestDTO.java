package com.carbuying.dto;

import com.carbuying.enums.InspectionCompany;
import com.carbuying.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDTO {
    private Long id;
    private Long customerId;
    private RequestStatus status;
    private String description;
    private InspectionCompany checkedByCompany;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer offerCount;
    private List<SupplierOfferDTO> offers;
}
