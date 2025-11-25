package com.smartlogis.hubservice.hubroute.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<HubRoute, HubRouteId> {

    Optional<HubRoute> findByIdAndDeletedAtIsNull(HubRouteId id);

    @Query("""
        SELECT r FROM HubRoute r
        WHERE r.deletedAt IS NULL
          AND (
               r.connection.startHubId = :hubId
            OR r.connection.endHubId = :hubId
            OR r.connection.relayHubId = :hubId
          )
    """)
    List<HubRoute> findAllByHubInvolved(UUID hubId);
}
