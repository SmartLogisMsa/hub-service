package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteDirectionApiFailedException extends AbstractException {

    public HubRouteDirectionApiFailedException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteDirectionApiFailedException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
