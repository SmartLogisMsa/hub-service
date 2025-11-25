package com.smartlogis.hubservice.hubroute.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record HubRouteCreateRequest(

        @Schema(
                description = "출발 허브 ID",
                example = "ef01f9d1-769e-44e5-94ef-56db8080265c"
        )
        @NotBlank(message = "출발 허브 ID는 필수입니다.")
        String startHubId,

        @Schema(
                description = "도착 허브 ID",
                example = "ef01f9d1-769e-44e5-94ef-56db8080265c"
        )
        @NotBlank(message = "도착 허브 ID는 필수입니다.")
        String endHubId

) {}
