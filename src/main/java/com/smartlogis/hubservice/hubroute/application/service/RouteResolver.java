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
        List<RouteInfo> flatRoutes = new ArrayList<>();

        Set<UUID> visitedHubs = new HashSet<>();      // 허브 단위 방문 체크
        Set<String> visitedEdges = new HashSet<>();   // 기존 start->end 조합 방문 체크

        resolveRecursive(startHubId, endHubId, flatRoutes, visitedHubs, visitedEdges);

        AtomicInteger seq = new AtomicInteger(1);
        flatRoutes.forEach(r -> r.setSequence(seq.getAndIncrement()));

        return flatRoutes;
    }

    private void resolveRecursive(
            UUID startHubId,
            UUID endHubId,
            List<RouteInfo> collector,
            Set<UUID> visitedHubs,
            Set<String> visitedEdges
    ) {
        String edgeKey = startHubId + "->" + endHubId;

        if (!visitedHubs.add(startHubId)) {
            log.error("허브 중복 방문 감지: {}", startHubId);
            return;
        }

        if (!visitedEdges.add(edgeKey)) {
            log.error("조합 중복 방문 감지: {}", edgeKey);
            return;
        }

        HubRouteDetailQueryResponse route =
                hubRouteQueryService.findDetail(startHubId, endHubId);

        // relay 없는 단일 hop
        if (route.relayHubId() == null) {
            addSegment(route, collector);
            return;
        }

        UUID relay = route.relayHubId();

        // relay 허브 이미 방문한 경우 중단
        if (visitedHubs.contains(relay)) {
            log.error("릴레이 허브 중복 방문 감지: {}", relay);
            return;
        }

        // 좌측: start → relay
        resolveRecursive(startHubId, relay, collector, visitedHubs, visitedEdges);

        // 우측: relay → end
        resolveRecursive(relay, endHubId, collector, visitedHubs, visitedEdges);
    }

    private void addSegment(HubRouteDetailQueryResponse route,
                            List<RouteInfo> collector) {

        String departureAddress =
                hubQueryService.getAddressByHubId(route.startHubId().toString());
        String destinationAddress =
                hubQueryService.getAddressByHubId(route.endHubId().toString());

        collector.add(
                RouteInfo.builder()
                        .departureHubId(route.startHubId())
                        .departureAddress(departureAddress)
                        .destinationHubId(route.endHubId())
                        .destinationAddress(destinationAddress)
                        .expectedDistanceKm(route.expectedDistanceKm())
                        .expectedDurationMin(route.expectedDurationMin())
                        .build()
        );
    }

}
