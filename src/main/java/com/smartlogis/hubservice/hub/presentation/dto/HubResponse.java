package com.smartlogis.hubservice.hub.presentation.dto;

import java.util.UUID;

public record HubResponse(
        UUID id,
        UUID managerId,
        String name,
        String address,
        double latitude,
        double longitude
) {}
