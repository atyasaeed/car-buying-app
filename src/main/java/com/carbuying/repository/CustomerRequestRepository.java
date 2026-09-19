package com.carbuying.repository;

import com.carbuying.entity.CustomerRequest;
import com.carbuying.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Long> {
    Page<CustomerRequest> findByCustomerId(Long customerId, Pageable pageable);

    Page<CustomerRequest> findByStatus(RequestStatus status, Pageable pageable);

    @Query("SELECT cr FROM CustomerRequest cr WHERE cr.customerId = :customerId AND cr.status = :status")
    Page<CustomerRequest> findByCustomerIdAndStatus(
        @Param("customerId") Long customerId,
        @Param("status") RequestStatus status,
        Pageable pageable
    );

    @Query("SELECT COUNT(so) FROM SupplierOffer so WHERE so.customerRequest.id = :requestId")
    Long countOffersByRequestId(@Param("requestId") Long requestId);
}
