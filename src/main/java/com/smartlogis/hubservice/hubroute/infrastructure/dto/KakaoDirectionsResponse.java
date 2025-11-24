package com.smartlogis.hubservice.hubroute.infrastructure.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoDirectionsResponse {

    private List<Route> routes;

    @Getter
    @NoArgsConstructor
    public static class Route {
        private Summary summary;
    }

    @Getter
    @NoArgsConstructor
    public static class Summary {
        // 총 거리 (미터 단위)
        private int distance;

        // 총 소요 시간 (밀리초 단위)
        private int duration;
    }
}
