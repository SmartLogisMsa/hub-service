package com.smartlogis.hubservice.hub.event.message;

import java.io.Serializable;
import java.util.UUID;

public record HubDeletedMessage(
        UUID hubId
) implements Serializable {}
