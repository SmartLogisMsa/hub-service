package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hubroute.domain.HubConnection;
import com.smartlogis.hubservice.hubroute.domain.HubRoute;
import com.smartlogis.hubservice.hubroute.domain.HubRouteId;
import com.smartlogis.hubservice.hubroute.domain.RouteInfo;
import com.smartlogis.hubservice.hubroute.domain.HubRouteRepository;
import com.smartlogis.hubservice.hubroute.domain.exception.*;
import com.smartlogis.hubservice.hubroute.infrastructure.service.KakaoDirectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteCreateService {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final KakaoDirectionsService kakaoDirectionsService;
    private final RelayHubFinder relayHubFinder;

    // P2P 거리 기준
    private static final double P2P_DISTANCE_THRESHOLD_KM = 100.0;

    @Transactional
    public HubRoute createRoute(String startHubId, String endHubId) {

        // HubId 변환
        HubId startId = HubId.of(UUID.fromString(startHubId));
        HubId endId = HubId.of(UUID.fromString(endHubId));

        // 1. 허브 조회
        Hub startHub = hubRepository.findByIdAndStatusAndDeletedAtIsNull(startId, HubStatus.ACTIVE)
                .orElseThrow(() -> new HubRouteStartHubNotFoundException(HubRouteMessageCode.HUB_ROUTE_START_HUB_NOT_FOUND));

        Hub endHub = hubRepository.findByIdAndStatusAndDeletedAtIsNull(endId, HubStatus.ACTIVE)
                .orElseThrow(() -> new HubRouteEndHubNotFoundException(HubRouteMessageCode.HUB_ROUTE_END_HUB_NOT_FOUND));

        double startLat = startHub.getLocation().getLatitude();
        double startLng = startHub.getLocation().getLongitude();
        double endLat = endHub.getLocation().getLatitude();
        double endLng = endHub.getLocation().getLongitude();

        // 2. start → end 직통 거리 조회
        RouteInfo directInfo = kakaoDirectionsService.getRouteInfo(
                startLat, startLng,
                endLat, endLng
        );

        if (directInfo == null) {
            throw new HubRouteDirectionApiFailedException(HubRouteMessageCode.HUB_ROUTE_DIRECTION_API_FAILED);
        }

        // 3. P2P_DISTANCE_THRESHOLD_KM 이하 → P2P
        if (directInfo.getExpectedDistanceKm().doubleValue() <= P2P_DISTANCE_THRESHOLD_KM) {

            HubConnection connection = new HubConnection(
                    startId.getId(),
                    endId.getId(),
                    null
            );

            HubRoute route = new HubRoute(
                    HubRouteId.newId(),
                    connection,
                    directInfo
            );

            return hubRouteRepository.save(route);
        }

        // 4. P2P_DISTANCE_THRESHOLD_KM 초과 → Relay
        Hub relayHub = relayHubFinder.findRelayHub(startHub, endHub);
        if (relayHub == null) {
            throw new HubRouteRelayHubNotFoundException(HubRouteMessageCode.HUB_ROUTE_RELAY_HUB_NOT_FOUND);
        }

        // 5. start → relay
        RouteInfo srInfo = kakaoDirectionsService.getRouteInfo(
                startLat, startLng,
                relayHub.getLocation().getLatitude(),
                relayHub.getLocation().getLongitude()
        );

        // 6. relay → end
        RouteInfo reInfo = kakaoDirectionsService.getRouteInfo(
                relayHub.getLocation().getLatitude(),
                relayHub.getLocation().getLongitude(),
                endLat, endLng
        );

        // 7. Relay 구조 생성
        HubConnection connection = new HubConnection(
                startId.getId(),
                endId.getId(),
                relayHub.getId().getId()
        );

        double totalDistance = srInfo.getExpectedDistanceKm().doubleValue()
                + reInfo.getExpectedDistanceKm().doubleValue();

        int totalMinutes = srInfo.getExpectedDurationMin()
                + reInfo.getExpectedDurationMin();

        // 합산 RouteInfo 생성
        RouteInfo totalInfo = RouteInfo.of(
                BigDecimal.valueOf(totalDistance),
                totalMinutes
        );

        HubRoute route = new HubRoute(
                HubRouteId.newId(),
                connection,
                totalInfo
        );

        return hubRouteRepository.save(route);
    }
}
