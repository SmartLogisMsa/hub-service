package com.smartlogis.hubservice.hubroute.event.message;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryRouteEvent {

    private UUID orderId;
    private UUID productId;
    private UUID departureHubId;
    private UUID destinationHubId;
    private String address;
    private UUID receiptUserId;
    private List<RouteInfo> routes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RouteInfo {
        private Integer sequence;

        private UUID departureHubId;
        private String departureAddress;

        private UUID destinationHubId;
        private String destinationAddress;

        private BigDecimal expectedDistanceKm;
        private Integer expectedDurationMin;
    }
}
