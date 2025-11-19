package com.smartlogis.hubservice.hub.presentation.controller;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.hubservice.hub.application.service.HubCreateService;
import com.smartlogis.hubservice.hub.application.service.HubDeleteService;
import com.smartlogis.hubservice.hub.application.service.HubUpdateService;
import com.smartlogis.hubservice.hub.presentation.dto.HubCreateRequest;
import com.smartlogis.hubservice.hub.presentation.dto.HubResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubCreateService hubCreateService;
    private final HubUpdateService hubUpdateService;
    private final HubDeleteService hubDeleteService;

    // 허브 생성
    @PostMapping
    public ResponseEntity<ApiResponse<HubResponse>> createHub(
            @Valid @RequestBody HubCreateRequest request
    ) {
        HubResponse response = hubCreateService.create(request);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }

    // 허브 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HubResponse>> updateHub(
            @PathVariable("id") String id,
            @Valid @RequestBody HubUpdateRequest request
    ) {
        HubResponse response = hubUpdateService.update(id, request);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }

    // 허브 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHub(@PathVariable String id) {
        hubDeleteService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
