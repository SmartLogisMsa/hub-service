package com.smartlogis.hubservice.hub.presentation.controller;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hub.application.service.HubQueryService;
import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubListResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hubs")
@RequiredArgsConstructor
public class HubQueryController {

    private final HubQueryService hubQueryService;

    @GetMapping("/{id}")
    public ApiResponse<HubDetailResponse> getHubDetail(@PathVariable String id) {
        HubDetailResponse response = hubQueryService.findById(id);
        return ApiResponse.successWithDataOnly(response);
    }

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
