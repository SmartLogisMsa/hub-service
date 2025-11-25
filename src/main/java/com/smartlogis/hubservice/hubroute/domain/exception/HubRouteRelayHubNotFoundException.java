package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteRelayHubNotFoundException extends AbstractException {

    public HubRouteRelayHubNotFoundException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteRelayHubNotFoundException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
