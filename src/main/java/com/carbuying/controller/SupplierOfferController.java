package com.carbuying.controller;

import com.carbuying.dto.CreateSupplierOfferDTO;
import com.carbuying.dto.SupplierOfferDTO;
import com.carbuying.enums.OfferStatus;
import com.carbuying.service.SupplierOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/supplier-offers")
@RequiredArgsConstructor
public class SupplierOfferController {
    private final SupplierOfferService offerService;

    @PostMapping("/request/{requestId}")
    public ResponseEntity<SupplierOfferDTO> submitOffer(
            @PathVariable Long requestId,
            @Valid @RequestBody CreateSupplierOfferDTO dto) {
        SupplierOfferDTO created = offerService.submitOffer(requestId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<Page<SupplierOfferDTO>> listOffersByRequest(
            @PathVariable Long requestId,
            Pageable pageable) {
        Page<SupplierOfferDTO> offers = offerService.listOffersByRequest(requestId, pageable);
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<Page<SupplierOfferDTO>> listOffersBySupplier(
            @PathVariable Long supplierId,
            Pageable pageable) {
        Page<SupplierOfferDTO> offers = offerService.listOffersBySupplier(supplierId, pageable);
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<SupplierOfferDTO> getOffer(
            @PathVariable Long offerId) {
        SupplierOfferDTO offer = offerService.getOffer(offerId);
        return ResponseEntity.ok(offer);
    }

    @PatchMapping("/{offerId}/status")
    public ResponseEntity<SupplierOfferDTO> updateOfferStatus(
            @PathVariable Long offerId,
            @RequestParam OfferStatus status) {
        SupplierOfferDTO updated = offerService.updateOfferStatus(offerId, status);
        return ResponseEntity.ok(updated);
    }
}
