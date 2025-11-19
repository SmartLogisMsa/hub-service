package com.smartlogis.hubservice.hub.domain.exception;

import org.springframework.http.HttpStatus;
import com.smartlogis.common.exception.MessageCode;

public enum HubMessageCode implements MessageCode {

    // Hub 기본
    HUB_NOT_FOUND("HUB.NOT_FOUND", HttpStatus.NOT_FOUND),

    // 좌표/주소 관련
    HUB_ADDRESS_INVALID("HUB.ADDRESS_INVALID", HttpStatus.BAD_REQUEST),
    HUB_COORDINATE_NOT_FOUND("HUB.COORDINATE_NOT_FOUND", HttpStatus.BAD_REQUEST),
    HUB_INVALID_LOCATION("HUB.INVALID_LOCATION", HttpStatus.BAD_REQUEST),
    HUB_CANNOT_DELETE_ACTIVE("HUB.CANNOT_DELETE_ACTIVE", HttpStatus.BAD_REQUEST);

    private final String code;
    private final HttpStatus status;

    HubMessageCode(String code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
