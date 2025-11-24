package com.smartlogis.hubservice.hubroute.presentation;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteQueryService;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteDetailQueryResponse;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteListQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/hub-routes")
@RequiredArgsConstructor
public class HubRouteQueryController {

    private final HubRouteQueryService hubRouteQueryService;

    // 단건 조회 (캐싱)
    @GetMapping("/detail")
    public ApiResponse<HubRouteDetailQueryResponse> getHubRouteDetail(
            @RequestParam UUID startHubId,
            @RequestParam UUID endHubId
    ) {
        HubRouteDetailQueryResponse response =
                hubRouteQueryService.findDetail(startHubId, endHubId);

        return ApiResponse.successWithDataOnly(response);
    }

    // 목록 조회 + 검색(keyword) + 페이징
    @GetMapping
    public ApiResponse<PageResponse<HubRouteListQueryResponse>> searchHubRoutes(
            PageRequest pageRequest,
            @RequestParam(required = false) String keyword
    ) {
        PageResponse<HubRouteListQueryResponse> response =
                hubRouteQueryService.search(pageRequest, keyword);

        return ApiResponse.successWithDataOnly(response);
    }
}
