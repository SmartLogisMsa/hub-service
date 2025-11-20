package com.smartlogis.hubservice.hub.domain.exception;

import com.smartlogis.common.exception.AbstractException;

public class HubCannotSetDeletedByPatchException extends AbstractException {

    public HubCannotSetDeletedByPatchException(HubMessageCode messageCode) {
        super(messageCode);
    }

    public HubCannotSetDeletedByPatchException(HubMessageCode messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
    }
}
