package com.smartlogis.hubservice.hub.presentation.dto;

import com.smartlogis.hubservice.hub.domain.HubStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HubCreateRequest(
        @Schema(description = "허브 이름", example = "서울특별시 센터")
        @NotBlank(message = "허브 이름은 필수입니다.")
        String name,

        @Schema(description = "주소", example = "서울특별시 송파구 송파대로 55")
        @NotBlank(message = "주소는 필수입니다.")
        String address,

        @Schema(description = "허브 담당 관리자 ID (UUID)", example = "b6a1cbb9-f1ad-4c46-9cf9-bc9a6a9b7a32")
        @NotBlank(message = "managerId는 필수입니다.")
        String managerId,

        @Schema(description = "허브 상태", example = "ACTIVE")
        @NotNull(message = "허브 상태는 필수입니다.")
        HubStatus status
) {}
