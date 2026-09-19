package com.carbuying.mapper;

import com.carbuying.dto.CreateSupplierOfferDTO;
import com.carbuying.dto.SupplierOfferDTO;
import com.carbuying.entity.SupplierOffer;
import com.carbuying.enums.OfferStatus;
import org.springframework.stereotype.Component;

@Component
public class SupplierOfferMapper {
    public SupplierOffer toEntity(CreateSupplierOfferDTO dto) {
        return SupplierOffer.builder()
            .supplierId(dto.getSupplierId())
            .carDetails(dto.getCarDetails())
            .price(dto.getPrice())
            .status(OfferStatus.PENDING)
            .inspectionScore(0)
            .build();
    }

    public SupplierOfferDTO toDTO(SupplierOffer entity) {
        return SupplierOfferDTO.builder()
            .id(entity.getId())
            .supplierId(entity.getSupplierId())
            .customerRequestId(entity.getCustomerRequest().getId())
            .status(entity.getStatus())
            .inspectionScore(entity.getInspectionScore())
            .carDetails(entity.getCarDetails())
            .price(entity.getPrice())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
