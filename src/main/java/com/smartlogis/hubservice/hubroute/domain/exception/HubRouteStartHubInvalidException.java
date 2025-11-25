package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteStartHubInvalidException extends AbstractException {

    public HubRouteStartHubInvalidException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteStartHubInvalidException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
