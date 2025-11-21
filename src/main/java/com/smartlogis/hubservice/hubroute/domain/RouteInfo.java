package com.smartlogis.hubservice.hubroute.domain;

import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteDistanceInvalidException;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteDurationInvalidException;
import com.smartlogis.hubservice.hubroute.domain.exception.HubRouteMessageCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteInfo {

    @Column(name = "expected_distance_km", precision = 10, scale = 2, nullable = false)
    private BigDecimal expectedDistanceKm;

    @Column(name = "expected_duration_min", nullable = false)
    private Integer expectedDurationMin;

    public RouteInfo(BigDecimal expectedDistanceKm, Integer expectedDurationMin) {
        validate(expectedDistanceKm, expectedDurationMin);

        this.expectedDistanceKm = expectedDistanceKm;
        this.expectedDurationMin = expectedDurationMin;
    }

    private void validate(BigDecimal distance, Integer duration) {
        validateDistance(distance);
        validateDuration(duration);
    }

    private void validateDistance(BigDecimal distance) {
        if (distance == null || distance.compareTo(BigDecimal.ZERO) < 0) {
            throw new HubRouteDistanceInvalidException(HubRouteMessageCode.HUB_ROUTE_DISTANCE_INVALID);
        }
    }

    private void validateDuration(Integer duration) {
        if (duration == null || duration < 0) {
            throw new HubRouteDurationInvalidException(HubRouteMessageCode.HUB_ROUTE_DURATION_INVALID);
        }
    }

    public static RouteInfo of(BigDecimal distance, Integer duration) {
        return new RouteInfo(distance, duration);
    }
}
