package com.smartlogis.hubservice.hub.event;

import com.smartlogis.hubservice.hub.domain.HubId;
import com.smartlogis.hubservice.hub.domain.HubStatus;

public record HubStatusChangedEvent(
        HubId hubId,
        HubStatus status
) {}
