package com.smartlogis.hubservice.hub.application.service;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hub.domain.exception.HubCannotDeleteActiveException;
import com.smartlogis.hubservice.hub.domain.exception.HubMessageCode;
import com.smartlogis.hubservice.hub.domain.exception.HubNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubDeleteService {

    private final HubRepository hubRepository;

    @Transactional
    @CacheEvict(value = "hub", key = "#id")
    public void delete(String id) {

        Hub hub = hubRepository.findByIdAndDeletedAtIsNull(HubId.of(UUID.fromString(id)))
                .orElseThrow(() -> new HubNotFoundException(HubMessageCode.HUB_NOT_FOUND));

        if (hub.getStatus() == HubStatus.ACTIVE) {
            throw new HubCannotDeleteActiveException(HubMessageCode.HUB_CANNOT_DELETE_ACTIVE);
        }

        hub.softDelete();
        hubRepository.save(hub);
    }
}
