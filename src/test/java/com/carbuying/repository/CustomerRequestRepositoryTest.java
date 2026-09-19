package com.carbuying.repository;

import com.carbuying.entity.CustomerRequest;
import com.carbuying.enums.InspectionCompany;
import com.carbuying.enums.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRequestRepositoryTest {
    @Autowired
    private CustomerRequestRepository requestRepository;

    private CustomerRequest request1;
    private CustomerRequest request2;

    @BeforeEach
    void setUp() {
        request1 = CustomerRequest.builder()
            .customerId(1L)
            .description("Need a car")
            .status(RequestStatus.ACTIVE)
            .checkedByCompany(InspectionCompany.AUTO_CHECK_CO)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        request2 = CustomerRequest.builder()
            .customerId(2L)
            .description("Looking for SUV")
            .status(RequestStatus.CLOSED)
            .checkedByCompany(InspectionCompany.VEHI_VERIFY_INC)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        requestRepository.save(request1);
        requestRepository.save(request2);
    }

    @Test
    void testFindByCustomerId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerRequest> result = requestRepository.findByCustomerId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getCustomerId());
    }

    @Test
    void testFindByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerRequest> result = requestRepository.findByStatus(RequestStatus.ACTIVE, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(RequestStatus.ACTIVE, result.getContent().get(0).getStatus());
    }

    @Test
    void testFindByCustomerIdAndStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerRequest> result = requestRepository.findByCustomerIdAndStatus(1L, RequestStatus.ACTIVE, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getCustomerId());
        assertEquals(RequestStatus.ACTIVE, result.getContent().get(0).getStatus());
    }

    @Test
    void testFindByCustomerIdAndStatus_NoResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerRequest> result = requestRepository.findByCustomerIdAndStatus(1L, RequestStatus.CLOSED, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }
}
