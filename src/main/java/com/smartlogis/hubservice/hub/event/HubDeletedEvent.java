package com.smartlogis.hubservice.hub.event;

import com.smartlogis.hubservice.hub.domain.HubId;

public record HubDeletedEvent(HubId hubId) {}
