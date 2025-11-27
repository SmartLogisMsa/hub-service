package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.hubservice.hub.application.service.HubQueryService;
import com.smartlogis.hubservice.hubroute.event.message.DeliveryRouteEvent.RouteInfo;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteDetailQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteResolver {

    private final HubRouteQueryService hubRouteQueryService;
    private final HubQueryService hubQueryService;

    public List<RouteInfo> resolve(UUID startHubId, UUID endHubId) {

        List<RouteInfo> result = new ArrayList<>();

        // 전체 경로 조회 로그
        log.info("[RouteResolver] 전체 경로 조회 start={} end={}", startHubId, endHubId);

        HubRouteDetailQueryResponse fullRoute =
                hubRouteQueryService.findDetail(startHubId, endHubId);

        log.info("[RouteResolver] 전체 경로 결과: {}", fullRoute);

        UUID relay = fullRoute.relayHubId();

        if (relay == null) {
            log.info("[RouteResolver] 릴레이 없음 → 단일 hop 생성 start={} → end={}", startHubId, endHubId);
            result.add(buildHop(startHubId, endHubId));
        } else {
            log.info("[RouteResolver] 릴레이 있음 relay={} → 두 hop 생성", relay);

            // A → relay hop
            result.add(buildHop(startHubId, relay));

            // relay → C hop
            result.add(buildHop(relay, endHubId));
        }

        AtomicInteger seq = new AtomicInteger(1);
        result.forEach(r -> r.setSequence(seq.getAndIncrement()));

        log.info("[RouteResolver] 최종 결과 routes={}", result);

        return result;
    }

    private RouteInfo buildHop(UUID startHubId, UUID endHubId) {

        log.info("[RouteResolver][hop] hop 조회 시작 start={} → end={}", startHubId, endHubId);

        HubRouteDetailQueryResponse hopRoute =
                hubRouteQueryService.findDetail(startHubId, endHubId);

        log.info("[RouteResolver][hop] hop DB 조회 결과: {}", hopRoute);

        String departureAddress =
                hubQueryService.getAddressByHubId(startHubId.toString());
        String destinationAddress =
                hubQueryService.getAddressByHubId(endHubId.toString());

        log.info("[RouteResolver][hop] 주소 조회: departureAddress='{}', destinationAddress='{}'",
                departureAddress, destinationAddress);

        RouteInfo info = RouteInfo.builder()
                .departureHubId(startHubId)
                .departureHubAddress(departureAddress)
                .destinationHubId(endHubId)
                .destinationHubAddress(destinationAddress)
                .expectedDistanceKm(hopRoute.expectedDistanceKm())
                .expectedDurationMin(hopRoute.expectedDurationMin())
                .build();

        log.info("[RouteResolver][hop] 최종 hop 데이터: {}", info);

        return info;
    }

}
