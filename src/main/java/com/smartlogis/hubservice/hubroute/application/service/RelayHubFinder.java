package com.smartlogis.hubservice.hubroute.application.service;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.HubLocation;
import com.smartlogis.hubservice.hub.domain.HubStatus;
import com.smartlogis.hubservice.hub.domain.repository.HubVectorQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelayHubFinder {

    // midpoint에 가까운 허브 후보 개수 세팅
    private static final int DEFAULT_CANDIDATE_LIMIT = 3;

    private final HubVectorQueryRepository hubVectorQueryRepository;

    /**
     * 출발 Hub와 도착 Hub 사이에서 가장 적절한 Relay Hub를 찾는다.
     * 1) start-end의 중간 지점(midpoint)을 계산
     * 2) pgvector로 midpoint 근처의 허브 10개 조회
     * 3) 그 중 선분(start-end)에 가장 가까운 허브 1개 선택
     */
    @Transactional(readOnly = true)
    public Hub findRelayHub(Hub startHub, Hub endHub) {

        HubLocation startLoc = startHub.getLocation();
        HubLocation endLoc = endHub.getLocation();

        // 1) midpoint 계산
        Point midPoint = midPoint(startLoc, endLoc);

        // 2) midpoint 기준으로 가까운 허브 후보 N개(pgvector)
        List<Hub> candidates = hubVectorQueryRepository.findNearestHubsByLocation(
                midPoint.lat(), midPoint.lng(), DEFAULT_CANDIDATE_LIMIT
        );

        // 3) start, end 허브 제외 후 선분에 가장 가까운 허브 계산
        return candidates.stream()
                .filter(h -> !h.getId().equals(startHub.getId()))
                .filter(h -> !h.getId().equals(endHub.getId()))
                .filter(h -> h.getStatus() == HubStatus.ACTIVE)
                .min(Comparator.comparingDouble(h -> {
                    HubLocation loc = h.getLocation();
                    return distanceToLine(
                            new Point(startLoc.getLatitude(), startLoc.getLongitude()),
                            new Point(endLoc.getLatitude(), endLoc.getLongitude()),
                            new Point(loc.getLatitude(), loc.getLongitude())
                    );
                }))
                .orElse(null);
    }

    /** start–end 중간 지점 계산 */
    private Point midPoint(HubLocation startLoc, HubLocation endLoc) {
        double midLat = (startLoc.getLatitude() + endLoc.getLatitude()) / 2.0;
        double midLng = (startLoc.getLongitude() + endLoc.getLongitude()) / 2.0;
        return new Point(midLat, midLng);
    }

    /** 선분 AB와 점 C 사이의 최단 거리
     * 거리 = |(C - A) × (B - A)| / |B - A|
     * A = start 허브
     * B = end 허브
     * C = 후보 허브
     * 값이 작을 수록 좋은 후보
     * */
    private double distanceToLine(Point A, Point B, Point C) {
        double ax = A.lng();
        double ay = A.lat();
        double bx = B.lng();
        double by = B.lat();
        double cx = C.lng();
        double cy = C.lat();

        double cross = Math.abs((bx - ax) * (cy - ay) - (by - ay) * (cx - ax));
        double ab = Math.hypot(bx - ax, by - ay);

        if (ab == 0) {
            return Double.MAX_VALUE;
        }

        return cross / ab;
    }

    private record Point(double lat, double lng) {}
}
