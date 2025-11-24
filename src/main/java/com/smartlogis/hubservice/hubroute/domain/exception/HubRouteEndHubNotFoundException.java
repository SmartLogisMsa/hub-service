package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteEndHubNotFoundException extends AbstractException {

    public HubRouteEndHubNotFoundException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteEndHubNotFoundException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
