package com.carbuying.controller;

import com.carbuying.dto.CreateCustomerRequestDTO;
import com.carbuying.dto.CustomerRequestDTO;
import com.carbuying.dto.UpdateCustomerRequestDTO;
import com.carbuying.enums.RequestStatus;
import com.carbuying.service.CustomerRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer-requests")
@RequiredArgsConstructor
public class CustomerRequestController {
    private final CustomerRequestService requestService;

    @PostMapping
    public ResponseEntity<CustomerRequestDTO> createRequest(
            @Valid @RequestBody CreateCustomerRequestDTO dto) {
        CustomerRequestDTO created = requestService.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerRequestDTO>> listRequests(
            @RequestParam(required = false) RequestStatus status,
            Pageable pageable) {
        Page<CustomerRequestDTO> requests = requestService.listRequests(status, pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<CustomerRequestDTO>> listRequestsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(required = false) RequestStatus status,
            Pageable pageable) {
        Page<CustomerRequestDTO> requests = requestService.listRequestsByCustomer(customerId, status, pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<CustomerRequestDTO> getRequest(
            @PathVariable Long requestId) {
        CustomerRequestDTO request = requestService.getRequest(requestId);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<CustomerRequestDTO> updateRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody UpdateCustomerRequestDTO dto) {
        CustomerRequestDTO updated = requestService.updateRequest(requestId, dto);
        return ResponseEntity.ok(updated);
    }
}
