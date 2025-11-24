package com.smartlogis.hubservice.hubroute.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HubRouteRepository extends JpaRepository<HubRoute, HubRouteId> {

    Optional<HubRoute> findByIdAndDeletedAtIsNull(HubRouteId id);

}
