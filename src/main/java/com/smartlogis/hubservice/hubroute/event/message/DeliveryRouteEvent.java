package com.smartlogis.hubservice.hubroute.event.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryRouteEvent {

    private UUID orderId;
    private UUID productId;
    private UUID departureHubId;
    private String departureHubAddress;
    private UUID destinationHubId;
    private String destinationHubAddress;
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
        private String departureHubAddress;

        private UUID destinationHubId;
        private String destinationHubAddress;

        private BigDecimal expectedDistanceKm;
        private Integer expectedDurationMin;
    }
}
