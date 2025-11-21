package com.smartlogis.hubservice.hubroute.domain;

import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteConnectionInvalidException;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteEndHubInvalidException;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteMessageCode;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteStartHubInvalidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class HubConnection {

    @Column(name = "start_hub_id", columnDefinition = "uuid", nullable = false)
    private UUID startHubId;

    @Column(name = "end_hub_id", columnDefinition = "uuid", nullable = false)
    private UUID endHubId;

    @Column(name = "relay_hub_id", columnDefinition = "uuid")
    private UUID relayHubId;

    protected HubConnection() {}

    public HubConnection(UUID startHubId, UUID endHubId, UUID relayHubId) {
        validate(startHubId, endHubId);

        this.startHubId = startHubId;
        this.endHubId = endHubId;
        this.relayHubId = relayHubId;
    }

    private void validate(UUID startHubId, UUID endHubId) {
        validateStart(startHubId);
        validateEnd(endHubId);
        validateConnection(startHubId, endHubId);
    }

    private void validateStart(UUID startHubId) {
        if (startHubId == null) {
            throw new HubRouteStartHubInvalidException(HubRouteMessageCode.HUB_ROUTE_START_HUB_INVALID);
        }
    }

    private void validateEnd(UUID endHubId) {
        if (endHubId == null) {
            throw new HubRouteEndHubInvalidException(HubRouteMessageCode.HUB_ROUTE_END_HUB_INVALID);
        }
    }

    private void validateConnection(UUID startHubId, UUID endHubId) {
        if (startHubId != null && startHubId.equals(endHubId)) {
            throw new HubRouteConnectionInvalidException(HubRouteMessageCode.HUB_ROUTE_CONNECTION_INVALID);
        }
    }
}
