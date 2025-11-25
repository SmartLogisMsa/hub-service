package com.smartlogis.hubservice.hub.presentation.controller;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.hubservice.hub.application.service.HubCreateService;
import com.smartlogis.hubservice.hub.application.service.HubDeleteService;
import com.smartlogis.hubservice.hub.application.service.HubUpdateService;
import com.smartlogis.hubservice.hub.application.service.HubUpdateStatusService;
import com.smartlogis.hubservice.hub.presentation.dto.HubCreateRequest;
import com.smartlogis.hubservice.hub.presentation.dto.HubResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubStatusUpdateRequest;
import com.smartlogis.hubservice.hub.presentation.dto.HubUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubCreateService hubCreateService;
    private final HubUpdateService hubUpdateService;
    private final HubDeleteService hubDeleteService;
    private final HubUpdateStatusService hubUpdateStatusService;

    @Operation(summary = "허브 생성", description = "새로운 허브를 생성합니다.")
    @PreAuthorize("hasAnyRole('MASTER')")
    @PostMapping
    public ResponseEntity<ApiResponse<HubResponse>> createHub(
            @Valid @RequestBody HubCreateRequest request
    ) {
        HubResponse response = hubCreateService.create(request);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }

    @Operation(summary = "허브 수정", description = "허브 정보를 수정합니다.")
    @PreAuthorize("hasAnyRole('MASTER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HubResponse>> updateHub(
            @PathVariable("id") String id,
            @Valid @RequestBody HubUpdateRequest request
    ) {
        HubResponse response = hubUpdateService.update(id, request);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }


    @Operation(summary = "허브 삭제", description = "허브를 삭제합니다. (Soft Delete)")
    @PreAuthorize("hasAnyRole('MASTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHub(@PathVariable String id) {
        hubDeleteService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }


    @Operation(summary = "허브 상태 변경", description = "허브 상태(ACTIVE/INACTIVE)를 변경합니다.")
    @PreAuthorize("hasAnyRole('MASTER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable String id,
            @RequestBody HubStatusUpdateRequest request
    ) {
        hubUpdateStatusService.updateStatus(id, request.status());
        return ResponseEntity.ok(ApiResponse.success());
    }

}
