package com.smartlogis.hubservice.hubroute.event.message;


import java.util.UUID;

public record ProductToHubRouteEvent(
        UUID orderId,
     UUID productId,
     UUID departureHubId,    // 상품 서비스에서 보낸 출발 허브
     UUID destinationHubId,  // 업체 서비스에서 보낸 최종 도착 허브
     String address,
     UUID receiptUserId
) {}
