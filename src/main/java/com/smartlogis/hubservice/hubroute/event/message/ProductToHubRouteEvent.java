package com.smartlogis.hubservice.hubroute.event.message;

import java.util.UUID;
import lombok.Data;

@Data
public class ProductToHubRouteEvent {

    private UUID orderId;
    private UUID productId;
    private UUID departureHubId;    // 상품 서비스에서 보낸 출발 허브
    private UUID destinationHubId;  // 업체 서비스에서 보낸 최종 도착 허브
    private String address;
    private UUID receiptUserId;
}
