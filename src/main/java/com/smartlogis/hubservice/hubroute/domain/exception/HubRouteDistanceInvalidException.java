package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteDistanceInvalidException extends AbstractException {

    public HubRouteDistanceInvalidException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteDistanceInvalidException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
