package com.smartlogis.hubservice.hubroute.event;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hub.event.HubCreatedEvent;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouteEventHandler {

    private final HubRepository hubRepository;
    private final HubRouteCreateService hubRouteCreateService;

    @Async
    @EventListener
    public void handleHubCreated(HubCreatedEvent event) {

        Hub newHub = hubRepository.findByIdAndStatusAndDeletedAtIsNull(
                event.hubId(),
                HubStatus.ACTIVE
        ).orElse(null);

        if (newHub == null) {
            log.warn("[HubRouteEvent] 신규 허브 조회 실패 hubId={}", event.hubId().getId());
            return;
        }

        var activeHubs = hubRepository.findAllByStatusAndDeletedAtIsNull(HubStatus.ACTIVE);

        activeHubs.forEach(hub -> {
            if (hub.getId().equals(newHub.getId())) return;

            try {
                hubRouteCreateService.createRoute(
                        newHub.getId().getId().toString(),
                        hub.getId().getId().toString()
                );

                hubRouteCreateService.createRoute(
                        hub.getId().getId().toString(),
                        newHub.getId().getId().toString()
                );

            } catch (Exception e) {
                log.warn("[HubRouteEvent] Route 생성 실패 start={} end={} message={}",
                        newHub.getId().getId(),
                        hub.getId().getId(),
                        e.getMessage()
                );
            }
        });
    }
}
