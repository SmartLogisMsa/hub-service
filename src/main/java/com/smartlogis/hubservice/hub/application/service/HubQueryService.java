package com.smartlogis.hubservice.hub.application.service;

import com.smartlogis.hubservice.hub.domain.repository.HubQueryRepository;
import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubQueryService {

    private final HubQueryRepository hubQueryRepository;

    @Cacheable(value = "hubCache", key = "#id", unless = "#result == null")
    public HubDetailResponse findById(String id) {
        return hubQueryRepository.findById(id);
    }
}
