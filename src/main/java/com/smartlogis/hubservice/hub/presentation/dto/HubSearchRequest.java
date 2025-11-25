package com.smartlogis.hubservice.hub.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record HubSearchRequest(
        @Schema(description = "검색 키워드(허브명/지역명/상태)", example = "센터")
        String keyword
) {}
