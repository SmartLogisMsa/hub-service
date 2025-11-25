package com.smartlogis.hubservice.hubroute.presentation;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteCreateService;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteDeleteService;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteRecalculateService;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteCreateRequest;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteMapper;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/hub-routes")
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRouteCreateService hubRouteCreateService;
    private final HubRouteDeleteService hubRouteDeleteService;
    private final HubRouteRecalculateService hubRouteRecalculateService;

    @Operation(
            summary = "허브 경로 생성",
            description = """
                    두 허브(startHubId, endHubId) 사이의 경로를 생성합니다.
                    경로 계산은 Kakao Directions API를 기반으로 자동 수행됩니다.
                    """
    )
    @PreAuthorize("hasRole('MASTER')")
    @PostMapping
    public ResponseEntity<ApiResponse<HubRouteResponse>> create(
            @Valid @RequestBody HubRouteCreateRequest request
    ) {
        HubRouteResponse response = HubRouteMapper.from(
                hubRouteCreateService.createRoute(
                        request.startHubId(),
                        request.endHubId()
                )
        );
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }

    @Operation(
            summary = "허브 경로 재계산",
            description = "기존 허브 경로를 다시 계산합니다. 허브 위치가 수정되었을 때 사용합니다."
    )
    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/{id}/recalculate")
    public ResponseEntity<ApiResponse<HubRouteResponse>> recalculate(
            @Parameter(description = "HubRoute ID", example = "ef01f9d1-769e-44e5-94ef-56db8080265c")
            @PathVariable String id
    ) {
        HubRouteResponse response = HubRouteMapper.from(
                hubRouteRecalculateService.recalculate(id)
        );
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }


    @Operation(
            summary = "허브 경로 삭제",
            description = "특정 허브 경로를 삭제합니다. (Soft Delete)"
    )
    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoute(
            @Parameter(description = "HubRoute ID", example = "ef01f9d1-769e-44e5-94ef-56db8080265c")
            @PathVariable String id) {
        hubRouteDeleteService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
