package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.hubservice.hub.application.service.HubQueryService;
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
    private final HubQueryService hubQueryService;

    public List<RouteInfo> resolve(UUID startHubId, UUID endHubId) {
        List<RouteInfo> flatRoutes = new ArrayList<>();

        resolveRecursive(startHubId, endHubId, flatRoutes);

        AtomicInteger seq = new AtomicInteger(1);
        flatRoutes.forEach(r -> r.setSequence(seq.getAndIncrement()));

        return flatRoutes;
    }

    private void resolveRecursive(UUID startHubId, UUID endHubId, List<RouteInfo> collector) {

        HubRouteDetailQueryResponse route = hubRouteQueryService.findDetail(startHubId, endHubId);

        // 중간 허브가 없는 경우 → 단일 hop
        if (route.relayHubId() == null) {

            // 허브 주소 조회
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
