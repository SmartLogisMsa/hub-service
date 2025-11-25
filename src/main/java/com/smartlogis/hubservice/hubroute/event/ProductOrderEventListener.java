package com.smartlogis.hubservice.hubroute.event;

import com.smartlogis.hubservice.hubroute.event.message.ProductToHubRouteEvent;
import com.smartlogis.hubservice.hubroute.application.service.RouteResolver;
import com.smartlogis.hubservice.hubroute.event.message.DeliveryRouteEvent;
import com.smartlogis.hubservice.hubroute.infrastructure.messaging.DeliveryRouteEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductOrderEventListener {

    private final RouteResolver routeResolver;                 // 3단계에서 만들 것
    private final DeliveryRouteEventPublisher routePublisher;  // 4단계에서 만들 것

    @RabbitListener(queues = "smartlogis.product.order.created.queue")
    public void onProductOrderCreated(ProductToHubRouteEvent event) {

        log.info("[허브] 상품 이벤트 수신 productId={}, orderId={}",
                event.getProductId(), event.getOrderId());

        // routes 계산 (A→B→C 재귀 hop 포함)
        var routes = routeResolver.resolve(
                event.getDepartureHubId(),
                event.getDestinationHubId()
        );

        // 허브 → 배송 이벤트 발행
        DeliveryRouteEvent deliveryEvent = DeliveryRouteEvent.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .departureHubId(event.getDepartureHubId())
                .destinationHubId(event.getDestinationHubId())
                .address(event.getAddress())
                .receiptUserId(event.getReceiptUserId())
                .routes(routes)
                .build();

        routePublisher.publish(deliveryEvent);
    }
}
