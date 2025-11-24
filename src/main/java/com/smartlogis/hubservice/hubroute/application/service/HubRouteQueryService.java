package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hubroute.domain.HubRouteQueryRepository;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteMessageCode;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteNotFoundException;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteDetailQueryResponse;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteListQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubRouteQueryService {

    private final HubRouteQueryRepository hubRouteQueryRepository;

    @Cacheable(
            value = "hubRouteDetail",
            key = "#startHubId.toString() + '-' + #endHubId.toString()"
    )
    public HubRouteDetailQueryResponse findDetail(UUID startHubId, UUID endHubId) {
        HubRouteDetailQueryResponse response =
                hubRouteQueryRepository.findDetailByStartAndEnd(startHubId, endHubId);

        if (response == null) {
            throw new HubRouteNotFoundException(HubRouteMessageCode.HUB_ROUTE_NOT_FOUND);
        }

        return response;
    }

    public PageResponse<HubRouteListQueryResponse> search(PageRequest pageRequest, String keyword) {
        return hubRouteQueryRepository.search(pageRequest, keyword);
    }
}
