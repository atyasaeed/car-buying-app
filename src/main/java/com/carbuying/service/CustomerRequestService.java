package com.carbuying.service;

import com.carbuying.dto.CreateCustomerRequestDTO;
import com.carbuying.dto.CustomerRequestDTO;
import com.carbuying.dto.UpdateCustomerRequestDTO;
import com.carbuying.entity.CustomerRequest;
import com.carbuying.enums.RequestStatus;
import com.carbuying.exception.NotFoundException;
import com.carbuying.mapper.CustomerRequestMapper;
import com.carbuying.repository.CustomerRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerRequestService {
    private final CustomerRequestRepository requestRepository;
    private final CustomerRequestMapper requestMapper;
    private final InspectionService inspectionService;

    public CustomerRequestDTO createRequest(CreateCustomerRequestDTO dto) {
        CustomerRequest request = requestMapper.toEntity(dto);
        CustomerRequest saved = requestRepository.save(request);

        inspectionService.notifyInspectionCompany(saved);

        return requestMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<CustomerRequestDTO> listRequests(RequestStatus status, Pageable pageable) {
        Page<CustomerRequest> page;

        if (status != null) {
            page = requestRepository.findByStatus(status, pageable);
        } else {
            page = requestRepository.findAll(pageable);
        }

        return page.map(requestMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CustomerRequestDTO> listRequestsByCustomer(Long customerId, RequestStatus status, Pageable pageable) {
        Page<CustomerRequest> page;

        if (status != null) {
            page = requestRepository.findByCustomerIdAndStatus(customerId, status, pageable);
        } else {
            page = requestRepository.findByCustomerId(customerId, pageable);
        }

        return page.map(requestMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public CustomerRequestDTO getRequest(Long requestId) {
        CustomerRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new NotFoundException("Request not found with id: " + requestId));
        return requestMapper.toDTO(request);
    }

    public CustomerRequestDTO updateRequest(Long requestId, UpdateCustomerRequestDTO dto) {
        CustomerRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new NotFoundException("Request not found with id: " + requestId));

        request.setStatus(dto.getStatus());
        CustomerRequest updated = requestRepository.save(request);
        return requestMapper.toDTO(updated);
    }
}
