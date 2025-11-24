package com.smartlogis.hubservice.hubroute.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record HubRouteListQueryResponse(
        UUID id,
        UUID startHubId,
        UUID endHubId,
        UUID relayHubId,
        BigDecimal expectedDistanceKm,
        Integer expectedDurationMin,
        LocalDateTime createdAt
) {
}
