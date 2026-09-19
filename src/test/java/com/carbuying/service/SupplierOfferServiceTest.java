package com.carbuying.service;

import com.carbuying.dto.CreateSupplierOfferDTO;
import com.carbuying.dto.SupplierOfferDTO;
import com.carbuying.entity.CustomerRequest;
import com.carbuying.entity.SupplierOffer;
import com.carbuying.enums.InspectionCompany;
import com.carbuying.enums.OfferStatus;
import com.carbuying.enums.RequestStatus;
import com.carbuying.exception.BusinessException;
import com.carbuying.exception.NotFoundException;
import com.carbuying.mapper.SupplierOfferMapper;
import com.carbuying.repository.CustomerRequestRepository;
import com.carbuying.repository.SupplierOfferRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierOfferServiceTest {
    @Mock
    private SupplierOfferRepository offerRepository;

    @Mock
    private CustomerRequestRepository requestRepository;

    @Mock
    private SupplierOfferMapper offerMapper;

    @InjectMocks
    private SupplierOfferService offerService;

    private CreateSupplierOfferDTO createDTO;
    private CustomerRequest customerRequest;
    private SupplierOffer supplierOffer;
    private SupplierOfferDTO supplierOfferDTO;

    @BeforeEach
    void setUp() {
        createDTO = CreateSupplierOfferDTO.builder()
            .supplierId(1L)
            .carDetails("Toyota Camry 2020, Good condition")
            .price(new BigDecimal("15000"))
            .build();

        customerRequest = CustomerRequest.builder()
            .id(1L)
            .customerId(1L)
            .status(RequestStatus.ACTIVE)
            .description("Need a car")
            .checkedByCompany(InspectionCompany.AUTO_CHECK_CO)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        supplierOffer = SupplierOffer.builder()
            .id(1L)
            .supplierId(1L)
            .customerRequest(customerRequest)
            .status(OfferStatus.PENDING)
            .inspectionScore(0)
            .carDetails("Toyota Camry 2020, Good condition")
            .price(new BigDecimal("15000"))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        supplierOfferDTO = SupplierOfferDTO.builder()
            .id(1L)
            .supplierId(1L)
            .customerRequestId(1L)
            .status(OfferStatus.PENDING)
            .inspectionScore(0)
            .carDetails("Toyota Camry 2020, Good condition")
            .price(new BigDecimal("15000"))
            .build();
    }

    @Test
    void testSubmitOffer_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(customerRequest));
        when(offerRepository.countByRequestIdAndSupplierId(1L, 1L)).thenReturn(0L);
        when(offerMapper.toEntity(createDTO)).thenReturn(supplierOffer);
        when(offerRepository.save(any(SupplierOffer.class))).thenReturn(supplierOffer);
        when(offerMapper.toDTO(supplierOffer)).thenReturn(supplierOfferDTO);

        SupplierOfferDTO result = offerService.submitOffer(1L, createDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(OfferStatus.PENDING, result.getStatus());
    }

    @Test
    void testSubmitOffer_RequestNotActive() {
        customerRequest.setStatus(RequestStatus.CLOSED);
        when(requestRepository.findById(1L)).thenReturn(Optional.of(customerRequest));

        assertThrows(BusinessException.class, () -> offerService.submitOffer(1L, createDTO));
    }

    @Test
    void testSubmitOffer_RequestNotFound() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> offerService.submitOffer(1L, createDTO));
    }

    @Test
    void testSubmitOffer_SupplierAlreadySubmitted() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(customerRequest));
        when(offerRepository.countByRequestIdAndSupplierId(1L, 1L)).thenReturn(1L);

        assertThrows(BusinessException.class, () -> offerService.submitOffer(1L, createDTO));
    }

    @Test
    void testListOffersByRequest_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SupplierOffer> page = new PageImpl<>(List.of(supplierOffer), pageable, 1);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(customerRequest));
        when(offerRepository.findByCustomerRequestId(1L, pageable)).thenReturn(page);
        when(offerMapper.toDTO(supplierOffer)).thenReturn(supplierOfferDTO);

        Page<SupplierOfferDTO> result = offerService.listOffersByRequest(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testListOffersBySupplier_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SupplierOffer> page = new PageImpl<>(List.of(supplierOffer), pageable, 1);

        when(offerRepository.findBySupplierId(1L, pageable)).thenReturn(page);
        when(offerMapper.toDTO(supplierOffer)).thenReturn(supplierOfferDTO);

        Page<SupplierOfferDTO> result = offerService.listOffersBySupplier(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetOffer_Success() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(supplierOffer));
        when(offerMapper.toDTO(supplierOffer)).thenReturn(supplierOfferDTO);

        SupplierOfferDTO result = offerService.getOffer(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetOffer_NotFound() {
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> offerService.getOffer(1L));
    }

    @Test
    void testUpdateOfferStatus_Success() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(supplierOffer));
        when(offerRepository.save(any(SupplierOffer.class))).thenReturn(supplierOffer);
        when(offerMapper.toDTO(supplierOffer)).thenReturn(supplierOfferDTO);

        SupplierOfferDTO result = offerService.updateOfferStatus(1L, OfferStatus.ACCEPTED);

        assertNotNull(result);
        verify(offerRepository, times(1)).save(supplierOffer);
    }
}
