package com.smartlogis.hubservice.hub.domain.repository;

import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubListResponse;

public interface HubQueryRepository {

    HubDetailResponse findById(String id);

    PageResponse<HubListResponse> findAll(PageRequest request, String keyword);

}