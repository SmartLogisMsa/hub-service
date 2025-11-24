package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hubroute.domain.*;
import com.smartlogis.hubservice.hubroute.domain.exception.*;
import com.smartlogis.hubservice.hubroute.infrastructure.service.KakaoDirectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteRecalculateService {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final KakaoDirectionsService kakaoDirectionsService;
    private final RelayHubFinder relayHubFinder;

    private static final double P2P_DISTANCE_THRESHOLD_KM = 100.0;

    @Transactional
    public HubRoute recalculate(String routeId) {

        HubRoute route = hubRouteRepository.findByIdAndDeletedAtIsNull(
                HubRouteId.of(UUID.fromString(routeId))
        ).orElseThrow(() ->
                new HubRouteNotFoundException(HubRouteMessageCode.HUB_ROUTE_NOT_FOUND)
        );

        UUID startUUID = route.getConnection().getStartHubId();
        UUID endUUID = route.getConnection().getEndHubId();

        Hub startHub = hubRepository.findByIdAndStatusAndDeletedAtIsNull(
                HubId.of(startUUID), HubStatus.ACTIVE
        ).orElseThrow(() ->
                new HubRouteStartHubNotFoundException(HubRouteMessageCode.HUB_ROUTE_START_HUB_NOT_FOUND)
        );

        Hub endHub = hubRepository.findByIdAndStatusAndDeletedAtIsNull(
                HubId.of(endUUID), HubStatus.ACTIVE
        ).orElseThrow(() ->
                new HubRouteEndHubNotFoundException(HubRouteMessageCode.HUB_ROUTE_END_HUB_NOT_FOUND)
        );

        double startLat = startHub.getLocation().getLatitude();
        double startLng = startHub.getLocation().getLongitude();
        double endLat = endHub.getLocation().getLatitude();
        double endLng = endHub.getLocation().getLongitude();

        // ────────────────────────────────
        // 1) start → end 직통 계산
        // ────────────────────────────────
        RouteInfo directInfo = kakaoDirectionsService.getRouteInfo(
                startLat, startLng,
                endLat, endLng
        );

        if (directInfo == null) {
            throw new HubRouteDirectionApiFailedException(HubRouteMessageCode.HUB_ROUTE_DIRECTION_API_FAILED);
        }

        // ────────────────────────────────
        // 2) P2P_DISTANCE_THRESHOLD_KM 이하 → P2P
        // ────────────────────────────────
        if (directInfo.getExpectedDistanceKm().doubleValue() <= P2P_DISTANCE_THRESHOLD_KM) {

            route.updateConnection(new HubConnection(startUUID, endUUID, null));
            route.updateInfo(directInfo);
            return route;
        }

        // ────────────────────────────────
        // 3) Relay 필요
        // ────────────────────────────────
        Hub relayHub = relayHubFinder.findRelayHub(startHub, endHub);
        if (relayHub == null) {
            throw new HubRouteRelayHubNotFoundException(HubRouteMessageCode.HUB_ROUTE_RELAY_HUB_NOT_FOUND);
        }

        // start → relay
        RouteInfo srInfo = kakaoDirectionsService.getRouteInfo(
                startLat, startLng,
                relayHub.getLocation().getLatitude(),
                relayHub.getLocation().getLongitude()
        );

        // relay → end
        RouteInfo reInfo = kakaoDirectionsService.getRouteInfo(
                relayHub.getLocation().getLatitude(),
                relayHub.getLocation().getLongitude(),
                endLat, endLng
        );

        // 합산
        double totalDistance = srInfo.getExpectedDistanceKm().doubleValue()
                + reInfo.getExpectedDistanceKm().doubleValue();
        int totalMinutes = srInfo.getExpectedDurationMin() + reInfo.getExpectedDurationMin();

        route.updateConnection(
                new HubConnection(startUUID, endUUID, relayHub.getId().getId())
        );

        route.updateInfo(
                RouteInfo.of(BigDecimal.valueOf(totalDistance), totalMinutes)
        );

        return route;
    }
}
