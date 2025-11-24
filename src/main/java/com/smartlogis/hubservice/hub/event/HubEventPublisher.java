package com.smartlogis.hubservice.hub.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(HubCreatedEvent event) {
        publisher.publishEvent(event);
    }
}
