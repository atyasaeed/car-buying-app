package com.carbuying.mapper;

import com.carbuying.dto.CreateCustomerRequestDTO;
import com.carbuying.dto.CustomerRequestDTO;
import com.carbuying.entity.CustomerRequest;
import com.carbuying.enums.RequestStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomerRequestMapper {
    private final SupplierOfferMapper supplierOfferMapper;

    public CustomerRequestMapper(SupplierOfferMapper supplierOfferMapper) {
        this.supplierOfferMapper = supplierOfferMapper;
    }

    public CustomerRequest toEntity(CreateCustomerRequestDTO dto) {
        return CustomerRequest.builder()
            .customerId(dto.getCustomerId())
            .description(dto.getDescription())
            .checkedByCompany(dto.getCheckedByCompany())
            .status(RequestStatus.ACTIVE)
            .build();
    }

    public CustomerRequestDTO toDTO(CustomerRequest entity) {
        return CustomerRequestDTO.builder()
            .id(entity.getId())
            .customerId(entity.getCustomerId())
            .status(entity.getStatus())
            .description(entity.getDescription())
            .checkedByCompany(entity.getCheckedByCompany())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .offerCount(entity.getOffers() != null ? entity.getOffers().size() : 0)
            .offers(entity.getOffers() != null ?
                entity.getOffers().stream()
                    .map(supplierOfferMapper::toDTO)
                    .collect(Collectors.toList())
                : null)
            .build();
    }
}
