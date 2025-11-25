package com.smartlogis.hubservice.hubroute.domain;

import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteDetailQueryResponse;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteListQueryResponse;

import java.util.UUID;

public interface HubRouteQueryRepository {

    HubRouteDetailQueryResponse findDetailByStartAndEnd(UUID startHubId, UUID endHubId);

    PageResponse<HubRouteListQueryResponse> search(PageRequest pageRequest, String keyword);
}
