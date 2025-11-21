package com.smartlogis.hubservice.hub.presentation.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record HubDetailResponse(
        UUID id,
        UUID managerId,
        String name,
        String address,
        double latitude,
        double longitude,
        float[] locationVec,
        String status,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) implements Serializable {}
