package com.smartlogis.hubservice.hub.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HubRepository extends JpaRepository<Hub, HubId> {
    Optional<Hub> findByIdAndDeletedAtIsNull(HubId id);
}
