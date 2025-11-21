package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteNotFoundException extends AbstractException {

    public HubRouteNotFoundException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteNotFoundException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
