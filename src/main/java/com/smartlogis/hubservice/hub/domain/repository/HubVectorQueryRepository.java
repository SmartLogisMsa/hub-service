package com.smartlogis.hubservice.hub.domain.repository;

import com.smartlogis.hubservice.hub.domain.Hub;

import java.util.List;

public interface HubVectorQueryRepository {

    /**
     * 기준 좌표(위도, 경도)에서 가까운 허브를 벡터 기반으로 조회한다.
     * startHub, endHub는 서비스 단에서 제외 처리한다.
     */
    List<Hub> findNearestHubsByLocation(double latitude, double longitude, int limit);
}
