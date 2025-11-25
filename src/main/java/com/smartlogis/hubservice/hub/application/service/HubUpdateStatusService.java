package com.smartlogis.hubservice.hub.application.service;// com.smartlogis.hubservice.hub.application.service

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hub.domain.exception.HubCannotSetDeletedByPatchException;
import com.smartlogis.hubservice.hub.domain.exception.HubMessageCode;
import com.smartlogis.hubservice.hub.domain.exception.HubNotFoundException;
import com.smartlogis.hubservice.hub.event.HubStatusChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubUpdateStatusService {

    private final HubRepository hubRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void updateStatus(String id, HubStatus newStatus) {

        Hub hub = hubRepository.findByIdAndDeletedAtIsNull(HubId.of(UUID.fromString(id)))
                .orElseThrow(() -> new HubNotFoundException(HubMessageCode.HUB_NOT_FOUND));

        // ACTIVE → DELETED 는 삭제 서비스에서만 처리해야 함
        if (newStatus == HubStatus.DELETED) {
            throw new HubCannotSetDeletedByPatchException(HubMessageCode.HUB_CANNOT_SET_DELETED_BY_PATCH);
        }

        // 상태 변경
        hub.updateStatus(newStatus);

        applicationEventPublisher.publishEvent(
                new HubStatusChangedEvent(
                        hub.getId(),
                        hub.getStatus()
                )
        );
    }
}
