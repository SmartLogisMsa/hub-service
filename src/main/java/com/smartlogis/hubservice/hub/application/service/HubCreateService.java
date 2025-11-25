package com.smartlogis.hubservice.hub.application.service;

import com.smartlogis.hubservice.hub.domain.*;
import com.smartlogis.hubservice.hub.event.HubCreatedEvent;
import com.smartlogis.hubservice.hub.event.HubEventPublisher;
import com.smartlogis.hubservice.hub.presentation.dto.HubCreateRequest;
import com.smartlogis.hubservice.hub.presentation.dto.HubResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubCreateService {

    private final HubRepository hubRepository;
    private final HubAddressToCoords addressToCoords;
    private final HubEventPublisher hubEventPublisher;

    @Transactional
    public HubResponse create(HubCreateRequest request) {

        // 1. ManagerId
        UUID managerId = UUID.fromString(request.managerId());

        // 2. 주소 → 좌표 변환
        List<Double> coords = addressToCoords.convert(request.address());
        double lat = coords.get(0);
        double lon = coords.get(1);

        // 3. Location 생성
        HubLocation location = new HubLocation(
                request.address(),
                lat,
                lon
        );

        // 4. 허브 엔티티 생성
        Hub hub = new Hub(
                HubId.newId(),
                managerId,
                request.name(),
                location,
                request.status()
        );

        // 5. 저장
        Hub saved = hubRepository.save(hub);

        log.info("허브 생성 완료: id={}", saved.getId().getId());

        // 6. 라우트 자동 생성을 위한 이벤트 발행 (비동기)
        hubEventPublisher.publish(
                new HubCreatedEvent(saved.getId())
        );

        // 7. Response
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
