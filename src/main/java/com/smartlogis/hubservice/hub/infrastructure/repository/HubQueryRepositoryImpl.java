package com.smartlogis.hubservice.hub.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hub.domain.QHub;
import com.smartlogis.hubservice.hub.domain.repository.HubQueryRepository;
import com.smartlogis.hubservice.hub.presentation.dto.HubDetailResponse;
import com.smartlogis.hubservice.hub.presentation.dto.HubListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubQueryRepositoryImpl implements HubQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public HubDetailResponse findById(String id) {

        QHub hub = QHub.hub;


        // 기본 조건: 삭제되지 않은 허브만 조회
        BooleanExpression condition = hub.deletedAt.isNull()
                .and(hub.id.id.eq(UUID.fromString(id)));

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
                                .and(condition)
                )
                .fetchOne();
    }

    @Override
    public PageResponse<HubListResponse> findAll(PageRequest request, String keyword) {

        QHub hub = QHub.hub;

        int size = request.getSize();
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        // 기본 조건: 삭제되지 않은 허브만 조회
        BooleanExpression condition = hub.deletedAt.isNull();

        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                request.getPage(),
                size
        );

        // 통합 검색 조건: keyword가 있으면 이름/주소/상태 중 하나라도 포함되는 데이터만 조회
        if (keyword != null && !keyword.isBlank()) {
            condition = condition.and(
                    hub.name.containsIgnoreCase(keyword)
                            .or(hub.location.address.containsIgnoreCase(keyword))
                            .or(hub.status.stringValue().containsIgnoreCase(keyword))
            );
        }

        List<HubListResponse> content = query
                .select(Projections.constructor(
                        HubListResponse.class,
                        hub.id.id,
                        hub.managerId,
                        hub.name,
                        hub.location.address,
                        hub.status.stringValue(),
                        hub.createdAt,
                        hub.createdBy
                ))
                .from(hub)
                .where(condition)
                .orderBy(hub.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query
                .select(hub.count())
                .from(hub)
                .where(condition)
                .fetchOne();

        return new PageResponse<>(
                content,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                total
        );
    }
}
