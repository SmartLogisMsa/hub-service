package com.smartlogis.hubservice.hub.application.service;

import com.smartlogis.hubservice.hub.domain.*;
import com.smartlogis.hubservice.hub.domain.exception.HubMessageCode;
import com.smartlogis.hubservice.hub.domain.exception.HubNotFoundException;
import com.smartlogis.hubservice.hub.event.HubUpdatedEvent;
import com.smartlogis.hubservice.hub.presentation.dto.HubResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubUpdateService {

    private final HubRepository hubRepository;
    private final HubAddressToCoords addressToCoords;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @CacheEvict(value = "hub", key = "#id")
    public HubResponse update(String id, HubUpdateRequest request) {

        Hub hub = hubRepository.findByIdAndDeletedAtIsNull(HubId.of(UUID.fromString(id)))
                .orElseThrow(() -> new HubNotFoundException(HubMessageCode.HUB_NOT_FOUND));

        UUID managerId = UUID.fromString(request.managerId());

        boolean addressChanged = !hub.getLocation().getAddress().equals(request.address());
        boolean statusChanged = hub.getStatus() != request.status();

        HubLocation newLocation = hub.getLocation();

        if (addressChanged) {
            List<Double> coords = addressToCoords.convert(request.address());
            newLocation = new HubLocation(request.address(), coords.get(0), coords.get(1));
        }

        hub.update(
                request.name(),
                managerId,
                newLocation,
                request.status()
        );

        Hub saved = hubRepository.save(hub);

        // 위치 변경 또는 상태 변경이 있을 때 이벤트 발행
        if (addressChanged || statusChanged) {
            publisher.publishEvent(
                    new HubUpdatedEvent(
                            saved.getId(),
                            addressChanged,
                            statusChanged
                    )
            );
        }

        return new HubResponse(
                saved.getId().getId(),
                saved.getManagerId(),
                saved.getName(),
                saved.getLocation().getAddress(),
                saved.getLocation().getLatitude(),
                saved.getLocation().getLongitude()
        );
    }
}
