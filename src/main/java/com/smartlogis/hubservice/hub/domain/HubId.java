package com.smartlogis.hubservice.hub.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class HubId {

    @Column(name = "id")
    private UUID id;

    private HubId(UUID id) {
        this.id = id;
    }

    public static HubId newId() {
        return new HubId(UUID.randomUUID());
    }

    public static HubId of(UUID id) {
        return new HubId(id);
    }
}
