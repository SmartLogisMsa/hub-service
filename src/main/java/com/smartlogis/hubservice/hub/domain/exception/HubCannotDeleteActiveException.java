package com.smartlogis.hubservice.hub.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubCannotDeleteActiveException extends AbstractException {

    public HubCannotDeleteActiveException(HubMessageCode messageCode) {
        super(messageCode);
    }

    public HubCannotDeleteActiveException(HubMessageCode messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
    }
}
