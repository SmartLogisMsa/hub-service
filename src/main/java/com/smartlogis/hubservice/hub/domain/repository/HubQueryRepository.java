package com.smartlogis.hubservice.hub.domain.repository;

import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubListResponse;

import java.util.Optional;

public interface HubQueryRepository {

    HubDetailResponse findById(String id);

    PageResponse<HubListResponse> findAll(PageRequest request, String keyword);

}