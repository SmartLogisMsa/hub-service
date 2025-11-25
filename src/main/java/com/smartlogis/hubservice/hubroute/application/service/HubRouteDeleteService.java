package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.hubservice.hubroute.domain.HubRoute;
import com.smartlogis.hubservice.hubroute.domain.HubRouteId;
import com.smartlogis.hubservice.hubroute.domain.HubRouteRepository;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteMessageCode;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteDeleteService {

    private final HubRouteRepository hubRouteRepository;

    @Transactional
    public void delete(String routeId) {

        HubRouteId id = HubRouteId.of(UUID.fromString(routeId));

        HubRoute route = hubRouteRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new HubRouteNotFoundException(HubRouteMessageCode.HUB_ROUTE_NOT_FOUND));

        route.softDelete();
    }
}
