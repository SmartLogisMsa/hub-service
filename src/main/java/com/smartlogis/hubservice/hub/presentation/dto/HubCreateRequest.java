package com.smartlogis.hubservice.hub.presentation.dto;

import com.smartlogis.hubservice.hub.domain.HubStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HubCreateRequest(
        @NotBlank(message = "허브 이름은 필수입니다.")
        String name,

        @NotBlank(message = "주소는 필수입니다.")
        String address,

        @NotBlank(message = "managerId는 필수입니다.")
        String managerId,

        @NotNull(message = "허브 상태는 필수입니다.")
        HubStatus status
) {}
