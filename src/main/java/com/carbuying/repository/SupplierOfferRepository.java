package com.carbuying.repository;

import com.carbuying.entity.SupplierOffer;
import com.carbuying.enums.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierOfferRepository extends JpaRepository<SupplierOffer, Long> {
    Page<SupplierOffer> findByCustomerRequestId(Long customerRequestId, Pageable pageable);

    Page<SupplierOffer> findBySupplierId(Long supplierId, Pageable pageable);

    Page<SupplierOffer> findByStatus(OfferStatus status, Pageable pageable);

    @Query("SELECT so FROM SupplierOffer so WHERE so.customerRequest.id = :requestId AND so.supplierId = :supplierId")
    Optional<SupplierOffer> findByRequestIdAndSupplierId(
        @Param("requestId") Long requestId,
        @Param("supplierId") Long supplierId
    );

    @Query("SELECT COUNT(so) FROM SupplierOffer so WHERE so.customerRequest.id = :requestId AND so.supplierId = :supplierId")
    Long countByRequestIdAndSupplierId(
        @Param("requestId") Long requestId,
        @Param("supplierId") Long supplierId
    );
}
