package com.smartlogis.hubservice.hubroute.infrastructure.messaging;

import com.smartlogis.hubservice.hubroute.event.message.DeliveryRouteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryRouteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    // 허브 → 배송 라우팅 설정
    public static final String DELIVERY_ROUTE_EXCHANGE = "smartlogis.hubroute.order.exchange";

    public static final String DELIVERY_ROUTE_CREATED_ROUTING_KEY = "smartlogis.hubroute.order.route-created";

    public void publish(DeliveryRouteEvent event) {

        log.info("[허브 → 배송] DeliveryRouteEvent 발행 orderId={}, productId={}",
                event.getOrderId(), event.getProductId());
        if (event.getRoutes() == null) {
            log.warn("[허브 → 배송] routes=null");
        } else {
            log.info("[허브 → 배송] routes size={} / routes={}",
                    event.getRoutes().size(),
                    event.getRoutes());
        }
        rabbitTemplate.convertAndSend(
                DELIVERY_ROUTE_EXCHANGE,
                DELIVERY_ROUTE_CREATED_ROUTING_KEY,
                event
        );
    }
}
