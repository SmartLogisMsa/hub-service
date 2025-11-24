package com.smartlogis.hubservice.config.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class HubRabbitConfig {

    @Bean
    public TopicExchange hubExchange() {
        return new TopicExchange("hub.events");
    }
}
