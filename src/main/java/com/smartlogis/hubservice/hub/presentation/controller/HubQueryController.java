package com.smartlogis.hubservice.hub.presentation.controller;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.hubservice.hub.application.service.HubQueryService;
import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;
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
}
