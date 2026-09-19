package com.carbuying.service;

import com.carbuying.dto.CreateCustomerRequestDTO;
import com.carbuying.dto.CustomerRequestDTO;
import com.carbuying.dto.UpdateCustomerRequestDTO;
import com.carbuying.entity.CustomerRequest;
import com.carbuying.enums.InspectionCompany;
import com.carbuying.enums.RequestStatus;
import com.carbuying.exception.NotFoundException;
import com.carbuying.mapper.CustomerRequestMapper;
import com.carbuying.repository.CustomerRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRequestServiceTest {
    @Mock
    private CustomerRequestRepository requestRepository;

    @Mock
    private CustomerRequestMapper requestMapper;

    @Mock
    private InspectionService inspectionService;

    @InjectMocks
    private CustomerRequestService requestService;

    private CreateCustomerRequestDTO createDTO;
    private CustomerRequest customerRequest;
    private CustomerRequestDTO customerRequestDTO;

    @BeforeEach
    void setUp() {
        createDTO = CreateCustomerRequestDTO.builder()
            .customerId(1L)
            .description("Need to buy a car")
            .checkedByCompany(InspectionCompany.AUTO_CHECK_CO)
            .build();

        customerRequest = CustomerRequest.builder()
            .id(1L)
            .customerId(1L)
            .description("Need to buy a car")
            .status(RequestStatus.ACTIVE)
            .checkedByCompany(InspectionCompany.AUTO_CHECK_CO)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .offers(new ArrayList<>())
            .build();

        customerRequestDTO = CustomerRequestDTO.builder()
            .id(1L)
            .customerId(1L)
            .description("Need to buy a car")
            .status(RequestStatus.ACTIVE)
            .checkedByCompany(InspectionCompany.AUTO_CHECK_CO)
            .offerCount(0)
            .build();
    }

    @Test
    void testCreateRequest_Success() {
        when(requestMapper.toEntity(createDTO)).thenReturn(customerRequest);
        when(requestRepository.save(any(CustomerRequest.class))).thenReturn(customerRequest);
        when(requestMapper.toDTO(customerRequest)).thenReturn(customerRequestDTO);

        CustomerRequestDTO result = requestService.createRequest(createDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Need to buy a car", result.getDescription());
        verify(inspectionService, times(1)).notifyInspectionCompany(customerRequest);
    }

    @Test
    void testGetRequest_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(customerRequest));
        when(requestMapper.toDTO(customerRequest)).thenReturn(customerRequestDTO);

        CustomerRequestDTO result = requestService.getRequest(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetRequest_NotFound() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequest(1L));
    }

    @Test
    void testListRequests_ByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerRequest> page = new PageImpl<>(List.of(customerRequest), pageable, 1);

        when(requestRepository.findByStatus(RequestStatus.ACTIVE, pageable)).thenReturn(page);
        when(requestMapper.toDTO(customerRequest)).thenReturn(customerRequestDTO);

        Page<CustomerRequestDTO> result = requestService.listRequests(RequestStatus.ACTIVE, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateRequest_Success() {
        UpdateCustomerRequestDTO updateDTO = UpdateCustomerRequestDTO.builder()
            .status(RequestStatus.CLOSED)
            .build();

        when(requestRepository.findById(1L)).thenReturn(Optional.of(customerRequest));
        when(requestRepository.save(any(CustomerRequest.class))).thenReturn(customerRequest);
        when(requestMapper.toDTO(customerRequest)).thenReturn(customerRequestDTO);

        CustomerRequestDTO result = requestService.updateRequest(1L, updateDTO);

        assertNotNull(result);
        verify(requestRepository, times(1)).save(customerRequest);
    }

    @Test
    void testUpdateRequest_NotFound() {
        UpdateCustomerRequestDTO updateDTO = UpdateCustomerRequestDTO.builder()
            .status(RequestStatus.CLOSED)
            .build();

        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.updateRequest(1L, updateDTO));
    }
}
