package com.smartlogis.hubservice.hubroute.presentation.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record HubRouteResponse(

        UUID id,
        UUID startHubId,
        UUID endHubId,
        UUID relayHubId,
        BigDecimal expectedDistanceKm,
        Integer expectedDurationMin

) {}
