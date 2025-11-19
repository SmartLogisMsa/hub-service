package com.smartlogis.hubservice.hub.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubInvalidLocationException extends AbstractException {

    public HubInvalidLocationException(HubMessageCode messageCode) {
        super(messageCode);
    }

    public HubInvalidLocationException(HubMessageCode messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
    }
}
