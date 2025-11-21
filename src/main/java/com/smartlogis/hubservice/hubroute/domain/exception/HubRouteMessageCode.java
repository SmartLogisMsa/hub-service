package com.smartlogis.hubservice.hubroute.domain.exception;

import com.smartlogis.common.exception.MessageCode;
import org.springframework.http.HttpStatus;

public enum HubRouteMessageCode implements MessageCode {

    // 기본
    HUB_ROUTE_NOT_FOUND("HUB_ROUTE.NOT_FOUND", HttpStatus.NOT_FOUND),

    // 거리/시간 관련
    HUB_ROUTE_DISTANCE_INVALID("HUB_ROUTE.DISTANCE_INVALID", HttpStatus.BAD_REQUEST),
    HUB_ROUTE_DURATION_INVALID("HUB_ROUTE.DURATION_INVALID", HttpStatus.BAD_REQUEST),

    // 허브 연결 관련
    HUB_ROUTE_START_HUB_INVALID("HUB_ROUTE.START_HUB_INVALID", HttpStatus.BAD_REQUEST),
    HUB_ROUTE_END_HUB_INVALID("HUB_ROUTE.END_HUB_INVALID", HttpStatus.BAD_REQUEST),
    HUB_ROUTE_RELAY_HUB_INVALID("HUB_ROUTE.RELAY_HUB_INVALID", HttpStatus.BAD_REQUEST),
    HUB_ROUTE_CONNECTION_INVALID("HUB_ROUTE.CONNECTION_INVALID", HttpStatus.BAD_REQUEST);

    private final String code;
    private final HttpStatus status;

    HubRouteMessageCode(String code, HttpStatus status) {
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
