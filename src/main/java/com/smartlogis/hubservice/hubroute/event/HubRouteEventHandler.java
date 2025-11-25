package com.smartlogis.hubservice.hubroute.event;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hub.event.HubCreatedEvent;
import com.smartlogis.hubservice.hub.event.HubDeletedEvent;
import com.smartlogis.hubservice.hub.event.HubStatusChangedEvent;
import com.smartlogis.hubservice.hub.event.HubUpdatedEvent;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteCreateService;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteRecalculateService;
import com.smartlogis.hubservice.hubroute.domain.HubRoute;
import com.smartlogis.hubservice.hubroute.domain.HubRouteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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
     * 허브 생성 이벤트
     * - 새로 생성된 ACTIVE 허브와
     *   모든 ACTIVE 허브 간 라우트를 양방향으로 생성
     */
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
     * 허브 수정 이벤트
     * - locationChanged: 허브 위치 변경 시 해당 허브가 포함된 모든 라우트 재계산
     * - statusChanged : 상태 변경 시 라우트 상태 처리 (ACTIVE/INACTIVE)
     */
    @EventListener
    public void handleHubUpdated(HubUpdatedEvent event) {

        Hub hub = hubRepository.findById(event.hubId()).orElse(null);

        if (hub == null) {
            log.warn("[HubRouteEvent] 허브 수정 이벤트 처리 실패 – 허브 조회 안됨 hubId={}", event.hubId().getId());
            return;
        }

        if (event.statusChanged()) {
            handleStatusChange(hub);
            return;
        }

        // 위치 변경 → 해당 허브가 포함된 모든 라우트 재계산
        if (event.locationChanged()) {
            List<HubRoute> routes =
                    hubRouteRepository.findAllByHubInvolved(hub.getId().getId());

            for (HubRoute route : routes) {
                try {
                    hubRouteRecalculateService.recalculate(
                            route.getId().getId().toString()
                    );
                } catch (Exception e) {
                    log.warn(
                            "[HubRouteEvent] Route 재계산 실패 routeId={} message={}",
                            route.getId().getId(),
                            e.getMessage()
                    );
                }
            }
        }
    }

    /**
     * 허브 상태 변경 공통 처리
     * ACTIVE   → INACTIVE : 해당 허브가 포함된 모든 라우트 softDelete
     * INACTIVE → ACTIVE   : 모든 ACTIVE 허브와 라우트 재생성 (양방향)
     */
    private void handleStatusChange(Hub hub) {

        // INACTIVE: 허브 비활성화 → 라우트 soft delete
        if (hub.getStatus() == HubStatus.INACTIVE) {

            List<HubRoute> routes =
                    hubRouteRepository.findAllByHubInvolved(hub.getId().getId());

            for (HubRoute route : routes) {
                route.softDelete();
            }

            // DB 반영
            hubRouteRepository.saveAll(routes);

            return;
        }

        // ACTIVE: 허브 활성화 → 다른 ACTIVE 허브들과 라우트 재생성
        if (hub.getStatus() == HubStatus.ACTIVE) {

            List<Hub> activeHubs =
                    hubRepository.findAllByStatusAndDeletedAtIsNull(HubStatus.ACTIVE);

            for (Hub other : activeHubs) {
                if (other.getId().equals(hub.getId())) {
                    continue;
                }

                try {
                    hubRouteCreateService.createRoute(
                            hub.getId().getId().toString(),
                            other.getId().getId().toString()
                    );

                    hubRouteCreateService.createRoute(
                            other.getId().getId().toString(),
                            hub.getId().getId().toString()
                    );
                } catch (Exception e) {
                    log.warn(
                            "[HubRouteEvent] 상태 변경에 따른 Route 생성 실패 start={} end={} message={}",
                            hub.getId().getId(),
                            other.getId().getId(),
                            e.getMessage()
                    );
                }
            }
        }
    }

    /**
     * 허브 삭제 이벤트
     * - 해당 허브가 포함된 모든 라우트 soft delete
     */
    @EventListener
    public void handleHubDeleted(HubDeletedEvent event) {

        HubId hubId = event.hubId();

        List<HubRoute> routes = hubRouteRepository.findAllByHubInvolved(hubId.getId());

        for (HubRoute route : routes) {
            route.softDelete();
        }

        hubRouteRepository.saveAll(routes);
    }

    /**
     * 허브 상태 변경(PATCH) 이벤트
     * - PATCH 기반 status 변경도 HubUpdatedEvent와 동일하게
     *   handleStatusChange 를 사용하여 처리
     */
    @EventListener
    public void handleHubStatusChanged(HubStatusChangedEvent event) {

        Hub hub = hubRepository.findById(event.hubId()).orElse(null);

        if (hub == null) {
            log.warn("[HubRouteEvent] Hub 상태 변경 이벤트 처리 실패 – 허브 조회 안됨 hubId={}", event.hubId().getId());
            return;
        }

        try {
            handleStatusChange(hub);
        } catch (Exception e) {
            log.warn(
                    "[HubRouteEvent] 상태 변경 처리 중 예외 발생 hubId={} message={}",
                    event.hubId().getId(),
                    e.getMessage()
            );
        }
    }

}
