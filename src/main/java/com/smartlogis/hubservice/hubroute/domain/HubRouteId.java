package com.smartlogis.hubservice.hubroute.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class HubRouteId {

    @Column(name = "id")
    private UUID id;

    private HubRouteId(UUID id) {
        this.id = id;
    }

    public static HubRouteId newId() {
        return new HubRouteId(UUID.randomUUID());
    }

    public static HubRouteId of(UUID id) {
        return new HubRouteId(id);
    }
}
