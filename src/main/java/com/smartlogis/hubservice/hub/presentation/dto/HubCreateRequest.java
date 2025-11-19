package com.smartlogis.hubservice.hub.presentation.dto;

import com.smartlogis.hubservice.hub.domain.HubStatus;

public record HubCreateRequest(
        String name,
        String address,
        String managerId,
        HubStatus status
) {}
