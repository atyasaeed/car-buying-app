package com.carbuying.service;

import com.carbuying.entity.CustomerRequest;
import com.carbuying.enums.InspectionCompany;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InspectionService {

    public void notifyInspectionCompany(CustomerRequest request) {
        InspectionCompany company = request.getCheckedByCompany();

        switch (company) {
            case AUTO_CHECK_CO -> notifyAutoCheckCo(request);
            case VEHI_VERIFY_INC -> notifyVehiVerifyInc(request);
        }
    }

    private void notifyAutoCheckCo(CustomerRequest request) {
        log.info("Notifying AUTO_CHECK_CO for request ID: {}, Customer ID: {}, Description: {}",
            request.getId(), request.getCustomerId(), request.getDescription());

        try {
            simulateAutoCheckCoApi(request);
        } catch (Exception e) {
            log.error("Failed to notify AUTO_CHECK_CO for request ID: {}", request.getId(), e);
        }
    }

    private void notifyVehiVerifyInc(CustomerRequest request) {
        log.info("Notifying VEHI_VERIFY_INC for request ID: {}, Customer ID: {}, Description: {}",
            request.getId(), request.getCustomerId(), request.getDescription());

        try {
            simulateVehiVerifyIncApi(request);
        } catch (Exception e) {
            log.error("Failed to notify VEHI_VERIFY_INC for request ID: {}", request.getId(), e);
        }
    }

    private void simulateAutoCheckCoApi(CustomerRequest request) {
        log.debug("Simulating API call to AUTO_CHECK_CO for request: {}", request.getId());
    }

    private void simulateVehiVerifyIncApi(CustomerRequest request) {
        log.debug("Simulating API call to VEHI_VERIFY_INC for request: {}", request.getId());
    }
}
