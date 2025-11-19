package com.smartlogis.hubservice.hub.presentation.controller;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.hubservice.hub.application.service.HubCreateService;
import com.smartlogis.hubservice.hub.presentation.dto.HubCreateRequest;
import com.smartlogis.hubservice.hub.presentation.dto.HubResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubCreateService hubCreateService;

    @PostMapping
    public ApiResponse<HubResponse> createHub(@RequestBody HubCreateRequest request) {
        HubResponse response = hubCreateService.create(request);
        return ApiResponse.successWithDataOnly(response);
    }
}
