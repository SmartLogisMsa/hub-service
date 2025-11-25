package com.smartlogis.hubservice.hub.presentation.dto;

import com.smartlogis.hubservice.hub.domain.HubStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record HubStatusUpdateRequest(

        @Schema(description = "허브 상태", example = "ACTIVE")
        HubStatus status
) {}