package com.smartlogis.hubservice.hubroute.presentation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record HubRouteDetailQueryResponse(
        UUID id,
        UUID startHubId,
        UUID endHubId,
        UUID relayHubId,
        BigDecimal expectedDistanceKm,
        Integer expectedDurationMin,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) implements Serializable {
}
