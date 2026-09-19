package com.carbuying.repository;

import com.carbuying.entity.CustomerRequest;
import com.carbuying.entity.SupplierOffer;
import com.carbuying.enums.InspectionCompany;
import com.carbuying.enums.OfferStatus;
import com.carbuying.enums.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SupplierOfferRepositoryTest {
    @Autowired
    private SupplierOfferRepository offerRepository;

    @Autowired
    private CustomerRequestRepository requestRepository;

    private CustomerRequest customerRequest;
    private SupplierOffer offer1;
    private SupplierOffer offer2;

    @BeforeEach
    void setUp() {
        customerRequest = CustomerRequest.builder()
            .customerId(1L)
            .description("Need a car")
            .status(RequestStatus.ACTIVE)
            .checkedByCompany(InspectionCompany.AUTO_CHECK_CO)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        requestRepository.save(customerRequest);

        offer1 = SupplierOffer.builder()
            .supplierId(1L)
            .customerRequest(customerRequest)
            .status(OfferStatus.PENDING)
            .inspectionScore(85)
            .carDetails("Toyota Camry, Good condition")
            .price(new BigDecimal("15000"))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        offer2 = SupplierOffer.builder()
            .supplierId(2L)
            .customerRequest(customerRequest)
            .status(OfferStatus.PENDING)
            .inspectionScore(90)
            .carDetails("Honda Accord, Excellent condition")
            .price(new BigDecimal("18000"))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        offerRepository.save(offer1);
        offerRepository.save(offer2);
    }

    @Test
    void testFindByCustomerRequestId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SupplierOffer> result = offerRepository.findByCustomerRequestId(customerRequest.getId(), pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testFindBySupplierId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SupplierOffer> result = offerRepository.findBySupplierId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getSupplierId());
    }

    @Test
    void testFindByRequestIdAndSupplierId() {
        Optional<SupplierOffer> result = offerRepository.findByRequestIdAndSupplierId(customerRequest.getId(), 1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getSupplierId());
    }

    @Test
    void testFindByRequestIdAndSupplierId_NotFound() {
        Optional<SupplierOffer> result = offerRepository.findByRequestIdAndSupplierId(customerRequest.getId(), 999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCountByRequestIdAndSupplierId() {
        Long count = offerRepository.countByRequestIdAndSupplierId(customerRequest.getId(), 1L);

        assertEquals(1L, count);
    }

    @Test
    void testFindByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SupplierOffer> result = offerRepository.findByStatus(OfferStatus.PENDING, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }
}
