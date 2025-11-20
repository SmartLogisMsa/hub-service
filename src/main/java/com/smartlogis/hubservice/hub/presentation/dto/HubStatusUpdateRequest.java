package com.smartlogis.hubservice.hub.presentation.dto;

import com.smartlogis.hubservice.hub.domain.HubStatus;

public record HubStatusUpdateRequest(
        HubStatus status
) {}