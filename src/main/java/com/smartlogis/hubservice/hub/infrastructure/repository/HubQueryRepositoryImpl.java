package com.smartlogis.hubservice.hub.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smartlogis.hubservice.hub.domain.QHub;
import com.smartlogis.hubservice.hub.domain.repository.HubQueryRepository;
import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubQueryRepositoryImpl implements HubQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public HubDetailResponse findById(String id) {

        QHub hub = QHub.hub;

        return query
                .select(Projections.constructor(
                        HubDetailResponse.class,

                        hub.id.id,
                        hub.managerId,
                        hub.name,

                        hub.location.address,
                        hub.location.latitude,
                        hub.location.longitude,
                        hub.location.locationVec,

                        hub.status.stringValue(),

                        hub.createdAt,
                        hub.createdBy,
                        hub.updatedAt,
                        hub.updatedBy,
                        hub.deletedAt,
                        hub.deletedBy
                ))
                .from(hub)
                .where(
                        hub.id.id.eq(UUID.fromString(id))
                                .and(hub.deletedAt.isNull())
                )
                .fetchOne();
    }
}
