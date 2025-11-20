package com.smartlogis.hubservice.hub.domain.repository;

import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;

public interface HubQueryRepository {

    HubDetailResponse findById(String id);
}