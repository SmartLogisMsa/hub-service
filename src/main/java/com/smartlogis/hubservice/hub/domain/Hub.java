package com.smartlogis.hubservice.hub.domain;

import com.smartlogis.common.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.UUID;

@Getter
@ToString
@Entity
@Table(name = "p_hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub extends AbstractEntity {

    @EmbeddedId
    private HubId id;

    @Column(name = "manager_id", nullable = false, columnDefinition = "uuid")
    private UUID managerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Embedded
    private HubLocation location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private HubStatus status;

    public Hub(HubId id, UUID managerId, String name, HubLocation location, HubStatus status) {
        this.id = id;
        this.managerId = managerId;
        this.name = name;
        this.location = location;
        this.status = status;
    }

    public void update(String name, UUID managerId, HubLocation location, HubStatus status) {
        this.name = name;
        this.managerId = managerId;
        this.location = location;
        this.status = status;
    }

    public void softDelete() {
        super.delete();
    }
}
