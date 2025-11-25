package com.smartlogis.hubservice.hubroute.infrastructure.service;

import com.smartlogis.hubservice.hubroute.domain.RouteInfo;
import com.smartlogis.hubservice.hubroute.infrastructure.api.KakaoDirectionsClient;
import com.smartlogis.hubservice.hubroute.infrastructure.dto.KakaoDirectionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class KakaoDirectionsService {

    private final KakaoDirectionsClient kakaoDirectionsClient;

    /**
     * 카카오 길찾기 API를 호출하여 start → end 경로의 RouteInfo를 생성한다.
     * distance: meters → km
     * duration: milliseconds → minutes
     */
    public RouteInfo getRouteInfo(double startLat, double startLng,
                                  double endLat, double endLng) {

        KakaoDirectionsResponse response =
                kakaoDirectionsClient.getDirections(startLat, startLng, endLat, endLng);

        if (response == null || response.getRoutes() == null || response.getRoutes().isEmpty()) {
            return null;
        }

        KakaoDirectionsResponse.Summary summary =
                response.getRoutes().getFirst().getSummary();

        if (summary == null) {
            return null;
        }

        // meters → km
        BigDecimal distanceKm = BigDecimal.valueOf(summary.getDistance() / 1000.0);

        // ms → minutes
        int minutes = (int) Math.ceil(summary.getDuration() / 60.0);

        return RouteInfo.of(distanceKm, minutes);
    }
}
