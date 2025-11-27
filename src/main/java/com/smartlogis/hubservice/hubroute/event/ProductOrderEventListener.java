package com.smartlogis.hubservice.hubroute.event;

import com.smartlogis.hubservice.hub.application.service.HubQueryService;
import com.smartlogis.hubservice.hubroute.application.service.RouteResolver;
import com.smartlogis.hubservice.hubroute.event.message.DeliveryRouteEvent;
import com.smartlogis.hubservice.hubroute.event.message.ProductToHubRouteEvent;
import com.smartlogis.hubservice.hubroute.infrastructure.messaging.DeliveryRouteEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductOrderEventListener {

    private final RouteResolver routeResolver;
    private final DeliveryRouteEventPublisher routePublisher;
    private final HubQueryService hubQueryService;

    @RabbitListener(queues = "smartlogis.product.order.created.queue")
    public void onProductOrderCreated(ProductToHubRouteEvent event) {
        log.info("[허브] 상품 이벤트 수신 productId={}, orderId={}, receiptUserId={}, event: {}",
                event.productId(), event.orderId(), event.receiptUserId(), event);

        // routes 계산 (A→B→C 재귀 hop 포함)
        var routes = routeResolver.resolve(
                event.departureHubId(),
                event.destinationHubId()
        );
        String departureHubAddress =
                hubQueryService.getAddressByHubId(event.departureHubId().toString());
        String destinationHubAddress =
                hubQueryService.getAddressByHubId(event.destinationHubId().toString());

        // 허브 → 배송 이벤트 발행
        DeliveryRouteEvent deliveryEvent = DeliveryRouteEvent.builder()
                .orderId(event.orderId())
                .productId(event.productId())
                .departureHubId(event.departureHubId())
                .departureHubAddress(departureHubAddress)
                .destinationHubId(event.destinationHubId())
                .destinationHubAddress(destinationHubAddress)
                .address(event.address())
                .receiptUserId(event.receiptUserId())
                .routes(routes)
                .build();

        routePublisher.publish(deliveryEvent);
    }
}
