package com.carbuying.service;

import com.carbuying.dto.CreateSupplierOfferDTO;
import com.carbuying.dto.SupplierOfferDTO;
import com.carbuying.entity.CustomerRequest;
import com.carbuying.entity.SupplierOffer;
import com.carbuying.enums.OfferStatus;
import com.carbuying.enums.RequestStatus;
import com.carbuying.exception.BusinessException;
import com.carbuying.exception.NotFoundException;
import com.carbuying.mapper.SupplierOfferMapper;
import com.carbuying.repository.CustomerRequestRepository;
import com.carbuying.repository.SupplierOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierOfferService {
    private final SupplierOfferRepository offerRepository;
    private final CustomerRequestRepository requestRepository;
    private final SupplierOfferMapper offerMapper;

    public SupplierOfferDTO submitOffer(Long requestId, CreateSupplierOfferDTO dto) {
        CustomerRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new NotFoundException("Request not found with id: " + requestId));

        if (request.getStatus() != RequestStatus.ACTIVE) {
            throw new BusinessException("Cannot submit offer. Request is not active.");
        }

        Long existingOfferCount = offerRepository.countByRequestIdAndSupplierId(requestId, dto.getSupplierId());
        if (existingOfferCount > 0) {
            throw new BusinessException("Supplier has already submitted an offer for this request.");
        }

        SupplierOffer offer = offerMapper.toEntity(dto);
        offer.setCustomerRequest(request);
        SupplierOffer saved = offerRepository.save(offer);

        return offerMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<SupplierOfferDTO> listOffersByRequest(Long requestId, Pageable pageable) {
        requestRepository.findById(requestId)
            .orElseThrow(() -> new NotFoundException("Request not found with id: " + requestId));

        return offerRepository.findByCustomerRequestId(requestId, pageable)
            .map(offerMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<SupplierOfferDTO> listOffersBySupplier(Long supplierId, Pageable pageable) {
        return offerRepository.findBySupplierId(supplierId, pageable)
            .map(offerMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public SupplierOfferDTO getOffer(Long offerId) {
        SupplierOffer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new NotFoundException("Offer not found with id: " + offerId));
        return offerMapper.toDTO(offer);
    }

    public SupplierOfferDTO updateOfferStatus(Long offerId, OfferStatus status) {
        SupplierOffer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new NotFoundException("Offer not found with id: " + offerId));

        offer.setStatus(status);
        SupplierOffer updated = offerRepository.save(offer);
        return offerMapper.toDTO(updated);
    }
}
