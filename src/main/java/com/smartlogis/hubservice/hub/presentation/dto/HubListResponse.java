package com.smartlogis.hubservice.hub.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubListResponse(
        UUID id,
        UUID managerId,
        String name,
        String address,
        String status,
        LocalDateTime createdAt,
        String createdBy
) {}
