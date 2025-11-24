package com.smartlogis.hubservice.hubroute.domain;

import com.smartlogis.common.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "p_hub_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRoute extends AbstractEntity {

    @EmbeddedId
    private HubRouteId id;

    @Embedded
    private HubConnection connection;

    @Embedded
    private RouteInfo routeInfo;

    public HubRoute(HubRouteId id, HubConnection connection, RouteInfo routeInfo) {
        this.id = id;
        this.connection = connection;
        this.routeInfo = routeInfo;
    }

}
