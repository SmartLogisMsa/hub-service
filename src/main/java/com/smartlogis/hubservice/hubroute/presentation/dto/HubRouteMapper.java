package com.smartlogis.hubservice.hubroute.presentation.dto;

import com.smartlogis.hubservice.hubroute.domain.HubRoute;

public class HubRouteMapper {

    public static HubRouteResponse from(HubRoute route) {
        return new HubRouteResponse(
                route.getId().getId(),
                route.getConnection().getStartHubId(),
                route.getConnection().getEndHubId(),
                route.getConnection().getRelayHubId(),
                route.getRouteInfo().getExpectedDistanceKm(),
                route.getRouteInfo().getExpectedDurationMin()
        );
    }
}
