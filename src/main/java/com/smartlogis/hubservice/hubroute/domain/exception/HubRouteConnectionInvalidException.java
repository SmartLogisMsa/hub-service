package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteConnectionInvalidException extends AbstractException {

    public HubRouteConnectionInvalidException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteConnectionInvalidException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
