package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteDetailQueryResponse;
import com.smartlogis.hubservice.hubroute.event.message.DeliveryRouteEvent.RouteInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RouteResolver {

    private final HubRouteQueryService hubRouteQueryService;

    public List<RouteInfo> resolve(UUID startHubId, UUID endHubId) {
        List<RouteInfo> flatRoutes = new ArrayList<>();

        // (1) 재귀적으로 허브 경로를 단일 hop 리스트로 분해
        resolveRecursive(startHubId, endHubId, flatRoutes);

        // (2) sequence 번호 부여
        AtomicInteger seq = new AtomicInteger(1);
        flatRoutes.forEach(r -> r.setSequence(seq.getAndIncrement()));

        return flatRoutes;
    }

    private void resolveRecursive(UUID startHubId, UUID endHubId, List<RouteInfo> collector) {

        // DB에서 start → end 구간 조회
        HubRouteDetailQueryResponse route = hubRouteQueryService.findDetail(startHubId, endHubId);

        // 중간 허브가 없는 경우 → 단일 hop
        if (route.relayHubId() == null) {
            collector.add(
                    RouteInfo.builder()
                            .departureHubId(route.startHubId())
                            .destinationHubId(route.endHubId())
                            .expectedDistanceKm(route.expectedDistanceKm())
                            .expectedDurationMin(route.expectedDurationMin())
                            .build()
            );
            return;
        }

        // relayHubId가 있는 경우 → start → relay, relay → end 두 구간으로 나눔
        UUID relay = route.relayHubId();

        // 왼쪽 경로: start → relay
        resolveRecursive(startHubId, relay, collector);

        // 오른쪽 경로: relay → end
        resolveRecursive(relay, endHubId, collector);
    }
}
