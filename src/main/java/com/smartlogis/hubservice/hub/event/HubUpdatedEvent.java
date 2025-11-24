package com.smartlogis.hubservice.hub.event;

import com.smartlogis.hubservice.hub.domain.HubId;

public record HubUpdatedEvent(
        HubId hubId,
        boolean locationChanged,
        boolean statusChanged
) {}
