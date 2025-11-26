package com.smartlogis.hubservice.hubroute.presentation;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteQueryService;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteDetailQueryResponse;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteListQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/hubs/routes")
@RequiredArgsConstructor
public class HubRouteQueryController {

    private final HubRouteQueryService hubRouteQueryService;

    @Operation(
            summary = "허브 라우트 단건 조회",
            description = """
                    출발 허브 ID와 도착 허브 ID를 기준으로 허브 간 라우트 정보를 조회합니다.
                    내부적으로 캐시를 사용하여 자주 조회되는 경로는 빠르게 응답합니다.
                    """
    )
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping("/detail")
    public ApiResponse<HubRouteDetailQueryResponse> getHubRouteDetail(
            @RequestParam UUID startHubId,
            @RequestParam UUID endHubId
    ) {
        HubRouteDetailQueryResponse response =
                hubRouteQueryService.findDetail(startHubId, endHubId);

        return ApiResponse.successWithDataOnly(response);
    }

    @Operation(
            summary = "허브 라우트 목록 조회",
            description = """
                    허브 라우트 목록을 페이지네이션과 검색 조건으로 조회합니다.
                    - page: 페이지 번호 (0부터 시작)
                    - size: 페이지 크기
                    - keyword: km/min 검색 (선택값)
                    """
    )
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping
    public ApiResponse<PageResponse<HubRouteListQueryResponse>> searchHubRoutes(
            PageRequest pageRequest,
            @Parameter(
                    description = "검색 키워드 (km/min 등)",
                    example = "20"
            )
            @RequestParam(required = false) String keyword
    ) {
        PageResponse<HubRouteListQueryResponse> response =
                hubRouteQueryService.search(pageRequest, keyword);

        return ApiResponse.successWithDataOnly(response);
    }
}
