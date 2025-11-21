package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubRouteDurationInvalidException extends AbstractException {

    public HubRouteDurationInvalidException(HubRouteMessageCode messageCode) {
        super(messageCode);
    }

    public HubRouteDurationInvalidException(HubRouteMessageCode messageCode, Object... args) {
        super(messageCode, args);
    }
}
