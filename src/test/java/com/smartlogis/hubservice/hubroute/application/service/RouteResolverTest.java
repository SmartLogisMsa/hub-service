package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.hubservice.hubroute.event.message.DeliveryRouteEvent.RouteInfo;
import com.smartlogis.hubservice.hubroute.presentation.dto.HubRouteDetailQueryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class RouteResolverTest {

    @Mock
    private HubRouteQueryService hubRouteQueryService;

    @InjectMocks
    private RouteResolver routeResolver;

    UUID A = UUID.randomUUID();
    UUID B = UUID.randomUUID();
    UUID C = UUID.randomUUID();
    UUID D = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private HubRouteDetailQueryResponse route(UUID start, UUID end, UUID relay) {
        return new HubRouteDetailQueryResponse(
                UUID.randomUUID(),
                start,
                end,
                relay,
                BigDecimal.valueOf(10),
                30,
                LocalDateTime.now(),
                "SYSTEM",
                LocalDateTime.now(),
                "SYSTEM",
                null,
                null
        );
    }

    @Test
    void testResolveRouteWithMultipleRelays() {

        // A -> D (relay = B)
        when(hubRouteQueryService.findDetail(A, D))
                .thenReturn(route(A, D, B));

        // A -> B (relay = null)
        when(hubRouteQueryService.findDetail(A, B))
                .thenReturn(route(A, B, null));

        // B -> D (relay = C)
        when(hubRouteQueryService.findDetail(B, D))
                .thenReturn(route(B, D, C));

        // B -> C (relay = null)
        when(hubRouteQueryService.findDetail(B, C))
                .thenReturn(route(B, C, null));

        // C -> D (relay = null)
        when(hubRouteQueryService.findDetail(C, D))
                .thenReturn(route(C, D, null));

        // 실행
        List<RouteInfo> result = routeResolver.resolve(A, D);

        // 검증
        assertThat(result).hasSize(3);

        assertThat(result.get(0).getDepartureHubId()).isEqualTo(A);
        assertThat(result.get(0).getDestinationHubId()).isEqualTo(B);

        assertThat(result.get(1).getDepartureHubId()).isEqualTo(B);
        assertThat(result.get(1).getDestinationHubId()).isEqualTo(C);

        assertThat(result.get(2).getDepartureHubId()).isEqualTo(C);
        assertThat(result.get(2).getDestinationHubId()).isEqualTo(D);

        // sequence가 1, 2, 3 잘 매겨졌는지
        assertThat(result.get(0).getSequence()).isEqualTo(1);
        assertThat(result.get(1).getSequence()).isEqualTo(2);
        assertThat(result.get(2).getSequence()).isEqualTo(3);
    }
}
