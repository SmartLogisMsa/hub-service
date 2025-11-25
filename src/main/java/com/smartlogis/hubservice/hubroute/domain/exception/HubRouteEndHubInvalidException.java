package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteEndHubInvalidException extends AbstractException {

    public HubRouteEndHubInvalidException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteEndHubInvalidException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
