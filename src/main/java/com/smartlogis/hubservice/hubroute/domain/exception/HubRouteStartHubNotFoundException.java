package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteStartHubNotFoundException extends AbstractException {

    public HubRouteStartHubNotFoundException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteStartHubNotFoundException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
