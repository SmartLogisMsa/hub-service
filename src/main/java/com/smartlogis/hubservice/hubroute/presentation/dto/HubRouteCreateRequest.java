package com.smartlogis.hubservice.hubroute.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record HubRouteCreateRequest(

        @NotBlank(message = "출발 허브 ID는 필수입니다.")
        String startHubId,

        @NotBlank(message = "도착 허브 ID는 필수입니다.")
        String endHubId

) {}
