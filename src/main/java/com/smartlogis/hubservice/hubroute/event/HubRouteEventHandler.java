package com.smartlogis.hubservice.hubroute.event;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hub.event.HubCreatedEvent;
import com.smartlogis.hubservice.hub.event.HubDeletedEvent;
import com.smartlogis.hubservice.hub.event.HubUpdatedEvent;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteCreateService;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteRecalculateService;
import com.smartlogis.hubservice.hubroute.domain.HubRoute;
import com.smartlogis.hubservice.hubroute.domain.HubRouteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouteEventHandler {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final HubRouteCreateService hubRouteCreateService;
    private final HubRouteRecalculateService hubRouteRecalculateService;

    /**
     * 허브 생성 시 → 전체 허브와 라우트 생성 (양방향)
     */
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

        List<Hub> activeHubs = hubRepository.findAllByStatusAndDeletedAtIsNull(HubStatus.ACTIVE);

        for (Hub hub : activeHubs) {
            if (hub.getId().equals(newHub.getId())) {
                continue;
            }

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
                log.warn(
                        "[HubRouteEvent] Route 생성 실패 start={} end={} message={}",
                        newHub.getId().getId(),
                        hub.getId().getId(),
                        e.getMessage()
                );
            }
        }
    }

    /**
     * 허브 수정 시 → 위치 변경 / 상태 변경 이벤트 처리
     */
    @Async
    @EventListener
    public void handleHubUpdated(HubUpdatedEvent event) {

        Hub hub = hubRepository.findById(event.hubId()).orElse(null);

        if (hub == null) {
            return;
        }

        // 1) 위치 변경 → 해당 허브 포함 모든 라우트 재계산
        if (event.locationChanged()) {
            List<HubRoute> routes =
                    hubRouteRepository.findAllByHubInvolved(hub.getId().getId());

            for (HubRoute route : routes) {
                try {
                    hubRouteRecalculateService.recalculate(
                            route.getId().getId().toString()
                    );
                } catch (Exception ignored) {
                }
            }
        }

        // 2) 상태 변경 처리
        if (event.statusChanged()) {
            handleStatusChange(hub);
        }
    }

    /**
     * 허브 상태 변경 시 처리
     * ACTIVE → INACTIVE : 라우트 softDelete
     * INACTIVE → ACTIVE : 모든 ACTIVE 허브와 라우트 재생성
     */
    private void handleStatusChange(Hub hub) {

        // 현재 상태가 INACTIVE → 라우트 삭제
        if (hub.getStatus() == HubStatus.INACTIVE) {

            List<HubRoute> routes =
                    hubRouteRepository.findAllByHubInvolved(hub.getId().getId());

            for (HubRoute route : routes) {
                route.softDelete();
            }
            return;
        }

        // ACTIVE → 라우트 다시 생성
        if (hub.getStatus() == HubStatus.ACTIVE) {

            List<Hub> activeHubs =
                    hubRepository.findAllByStatusAndDeletedAtIsNull(HubStatus.ACTIVE);

            for (Hub other : activeHubs) {
                if (other.getId().equals(hub.getId())) {
                    continue;
                }

                hubRouteCreateService.createRoute(
                        hub.getId().getId().toString(),
                        other.getId().getId().toString()
                );

                hubRouteCreateService.createRoute(
                        other.getId().getId().toString(),
                        hub.getId().getId().toString()
                );
            }
        }
    }

    @Async
    @EventListener
    public void handleHubDeleted(HubDeletedEvent event) {

        HubId hubId = event.hubId();

        // 해당 허브가 포함된 모든 라우트 soft delete
        List<HubRoute> routes = hubRouteRepository.findAllByHubInvolved(hubId.getId());

        for (HubRoute route : routes) {
            route.softDelete();
        }
        hubRouteRepository.saveAll(routes);
    }

}
