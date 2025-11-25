package com.smartlogis.hubservice.hubroute.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 상품 → 허브 (consume)

    public static final String PRODUCT_ORDER_CREATED_QUEUE =
            "smartlogis.product.order.created.queue";

    public static final String PRODUCT_ORDER_CREATED_EXCHANGE =
            "smartlogis.product.exchange";

    public static final String PRODUCT_ORDER_CREATED_ROUTING_KEY =
            "smartlogis.product.order.created";

    @Bean
    public Queue productOrderCreatedQueue() {
        return new Queue(PRODUCT_ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public TopicExchange productOrderExchange() {
        return new TopicExchange(PRODUCT_ORDER_CREATED_EXCHANGE, true, false);
    }

    @Bean
    public Binding productOrderCreatedBinding() {
        return BindingBuilder.bind(productOrderCreatedQueue())
                .to(productOrderExchange())
                .with(PRODUCT_ORDER_CREATED_ROUTING_KEY);
    }


    // 허브 → 배송 (publish)
    public static final String HUBROUTE_ORDER_ROUTE_CREATED_QUEUE =
            "smartlogis.hubroute.order.route-created.queue";

    public static final String HUBROUTE_ORDER_EXCHANGE =
            "smartlogis.hubroute.order.exchange";

    public static final String HUBROUTE_ORDER_ROUTE_CREATED_ROUTING_KEY =
            "smartlogis.hubroute.order.route-created";

    @Bean
    public Queue hubRouteOrderRouteCreatedQueue() {
        return new Queue(HUBROUTE_ORDER_ROUTE_CREATED_QUEUE, true);
    }

    @Bean
    public TopicExchange hubRouteOrderExchange() {
        return new TopicExchange(HUBROUTE_ORDER_EXCHANGE, true, false);
    }

    @Bean
    public Binding hubRouteOrderBinding() {
        return BindingBuilder.bind(hubRouteOrderRouteCreatedQueue())
                .to(hubRouteOrderExchange())
                .with(HUBROUTE_ORDER_ROUTE_CREATED_ROUTING_KEY);
    }

    /* JSON 직렬화 */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
