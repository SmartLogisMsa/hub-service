package com.smartlogis.hubservice.hub.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubCoordinateNotFoundException extends AbstractException {

    public HubCoordinateNotFoundException(HubMessageCode messageCode) {
        super(messageCode);
    }

    public HubCoordinateNotFoundException(HubMessageCode messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
    }
}
