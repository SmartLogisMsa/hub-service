package com.smartlogis.hubservice.hub.application.service;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hub.domain.exception.HubNotFoundException;
import com.smartlogis.hubservice.hub.domain.exception.HubMessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubFinder {

    private final HubRepository hubRepository;

    public Hub findActiveHubById(HubId id) {
        return hubRepository.findByIdAndStatusAndDeletedAtIsNull(id, HubStatus.ACTIVE)
                .orElseThrow(() -> new HubNotFoundException(HubMessageCode.HUB_NOT_FOUND));
    }
}
