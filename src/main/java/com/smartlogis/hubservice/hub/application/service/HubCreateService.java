package com.smartlogis.hubservice.hub.application.service;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubLocation;
import com.smartlogis.hubservice.hub.domain.HubRepository;
import com.smartlogis.hubservice.hub.domain.HubAddressToCoords;
import com.smartlogis.hubservice.hub.presentation.dto.HubCreateRequest;
import com.smartlogis.hubservice.hub.presentation.dto.HubResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubCreateService {

    private final HubRepository hubRepository;
    private final HubAddressToCoords addressToCoords;

    @Transactional
    public HubResponse create(HubCreateRequest request) {
        UUID managerId = UUID.fromString(request.managerId());

        List<Double> coords = addressToCoords.convert(request.address());
        double lat = coords.get(0);
        double lon = coords.get(1);

        HubLocation location = new HubLocation(request.address(), lat, lon);

        Hub hub = new Hub(
                HubId.newId(),
                managerId,
                request.name(),
                location,
                request.status()
        );

        Hub saved = hubRepository.save(hub);

        return new HubResponse(
                saved.getId().getId(),
                saved.getManagerId(),
                saved.getName(),
                saved.getLocation().getAddress(),
                saved.getLocation().getLatitude(),
                saved.getLocation().getLongitude()
        );
    }
}
