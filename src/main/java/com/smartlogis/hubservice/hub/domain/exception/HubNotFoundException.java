package com.smartlogis.hubservice.hub.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubNotFoundException extends AbstractException {

    public HubNotFoundException(HubMessageCode messageCode) {
        super(messageCode);
    }

    public HubNotFoundException(HubMessageCode messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
    }
}
