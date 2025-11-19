package com.smartlogis.hubservice.hub.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubAddressInvalidException extends AbstractException {

    public HubAddressInvalidException(HubMessageCode messageCode) {
        super(messageCode);
    }

    public HubAddressInvalidException(HubMessageCode messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
    }
}
