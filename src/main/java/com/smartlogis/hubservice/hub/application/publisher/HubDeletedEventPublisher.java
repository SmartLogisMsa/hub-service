package com.smartlogis.hubservice.hub.application.publisher;

import com.smartlogis.hubservice.hub.event.message.HubDeletedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubDeletedEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishHubDeleted(UUID hubId) {
        HubDeletedMessage message = new HubDeletedMessage(hubId);

        rabbitTemplate.convertAndSend(
                "hub.events",
                "hub.deleted",
                message
        );
    }
}
