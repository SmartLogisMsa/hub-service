package com.smartlogis.hubservice.hubroute.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smartlogis.common.presentation.dto.PageRequest;
import com.smartlogis.common.presentation.dto.PageResponse;
import com.smartlogis.hubservice.hubroute.domain.HubRouteQueryRepository;
import com.smartlogis.hubservice.hubroute.domain.QHubRoute;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteDetailQueryResponse;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteListQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRouteQueryRepositoryImpl implements HubRouteQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public HubRouteDetailQueryResponse findDetailByStartAndEnd(UUID startHubId, UUID endHubId) {

        QHubRoute hubRoute = QHubRoute.hubRoute;

        BooleanExpression condition = hubRoute.deletedAt.isNull()
                .and(hubRoute.connection.startHubId.eq(startHubId))
                .and(hubRoute.connection.endHubId.eq(endHubId));

        return query
                .select(Projections.constructor(
                        HubRouteDetailQueryResponse.class,
                        hubRoute.id.id,
                        hubRoute.connection.startHubId,
                        hubRoute.connection.endHubId,
                        hubRoute.connection.relayHubId,
                        hubRoute.routeInfo.expectedDistanceKm,
                        hubRoute.routeInfo.expectedDurationMin,
                        hubRoute.createdAt,
                        hubRoute.createdBy,
                        hubRoute.updatedAt,
                        hubRoute.updatedBy,
                        hubRoute.deletedAt,
                        hubRoute.deletedBy
                ))
                .from(hubRoute)
                .where(condition)
                .fetchOne();
    }

    @Override
    public PageResponse<HubRouteListQueryResponse> search(PageRequest request, String keyword) {

        QHubRoute hubRoute = QHubRoute.hubRoute;

        // size 유효성 체크
        int size = request.getSize();
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                request.getPage(),
                size
        );

        // 기본 조건: 삭제되지 않은 데이터
        BooleanExpression condition = hubRoute.deletedAt.isNull();

        /*
            keyword가 있을 경우
            expectedDistanceKm / expectedDurationMin 에만 검색 적용
        */
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.trim();

            condition = condition.and(
                    hubRoute.routeInfo.expectedDistanceKm.stringValue().containsIgnoreCase(kw)
                            .or(hubRoute.routeInfo.expectedDurationMin.stringValue().containsIgnoreCase(kw))
            );
        }

        // 목록 조회
        List<HubRouteListQueryResponse> content = query
                .select(Projections.constructor(
                        HubRouteListQueryResponse.class,
                        hubRoute.id.id,
                        hubRoute.connection.startHubId,
                        hubRoute.connection.endHubId,
                        hubRoute.connection.relayHubId,
                        hubRoute.routeInfo.expectedDistanceKm,
                        hubRoute.routeInfo.expectedDurationMin,
                        hubRoute.createdAt
                ))
                .from(hubRoute)
                .where(condition)
                .orderBy(hubRoute.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total count
        long total = query
                .select(hubRoute.count())
                .from(hubRoute)
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
