package com.smartlogis.hubservice.hubroute.presentation;

import com.smartlogis.common.presentation.ApiResponse;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteCreateService;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteDeleteService;
import com.smartlogis.hubservice.hubroute.application.service.HubRouteRecalculateService;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteCreateRequest;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteMapper;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/hub-routes")
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRouteCreateService hubRouteCreateService;
    private final HubRouteDeleteService hubRouteDeleteService;
    private final HubRouteRecalculateService hubRouteRecalculateService;

    @PostMapping
    public ResponseEntity<ApiResponse<HubRouteResponse>> create(
            @Valid @RequestBody HubRouteCreateRequest request
    ) {

        var route = hubRouteCreateService.createRoute(
                request.startHubId(),
                request.endHubId()
        );

        ApiResponse<HubRouteResponse> body = ApiResponse.successWithDataOnly(
                HubRouteMapper.from(route)
        );

        return ResponseEntity.ok(
                ApiResponse.successWithDataOnly(HubRouteMapper.from(route))
        );
    }

    @PutMapping("/{id}/recalculate")
    public ResponseEntity<ApiResponse<HubRouteResponse>> recalculate(@PathVariable String id) {

        var route = hubRouteRecalculateService.recalculate(id);

        return ResponseEntity.ok(
                ApiResponse.successWithDataOnly(
                        HubRouteMapper.from(route)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoute(@PathVariable String id) {

        hubRouteDeleteService.delete(id);

        return ResponseEntity.ok(ApiResponse.success());
    }
}
