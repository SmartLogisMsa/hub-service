package com.smartlogis.hubservice.hub.presentation.controller;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hub.application.service.HubQueryService;
import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubListResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HubQueryController {

    private final HubQueryService hubQueryService;

    @Operation(summary = "허브 단건 조회", description = "허브 ID를 통해 단건 정보를 조회합니다.")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping("/{id}")
    public ApiResponse<HubDetailResponse> getHubDetail(
            @Parameter(description = "허브 ID", example = "a6d63bf0-9f4d-45d0-a0df-f573cd3d8f2a")
            @PathVariable String id) {
        HubDetailResponse response = hubQueryService.findById(id);
        return ApiResponse.successWithDataOnly(response);
    }

    @Operation(summary = "허브 목록 조회", description = "검색 조건(keyword)과 페이지네이션을 기반으로 허브 목록을 조회합니다.")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping
    public ApiResponse<PageResponse<HubListResponse>> getHubs(
            PageRequest pageRequest,
            @ModelAttribute HubSearchRequest searchRequest
    ) {
        return ApiResponse.successWithDataOnly(
                hubQueryService.findAll(pageRequest, searchRequest)
        );
    }


}
