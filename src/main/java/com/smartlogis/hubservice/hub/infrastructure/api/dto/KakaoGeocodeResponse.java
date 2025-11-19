package com.smartlogis.hubservice.hub.infrastructure.api.dto;

import java.util.List;

public record KakaoGeocodeResponse(
        Meta meta,
        List<Document> documents
) {
    public record Meta(
            Integer total_count,
            Integer pageable_count,
            Boolean is_end
    ) {}

    public record Document(
            String address_name,
            String address_type,
            String x,
            String y,
            Address address,
            RoadAddress road_address
    ) {}

    public record Address(
            String address_name,
            String region_1depth_name,
            String region_2depth_name,
            String region_3depth_name,
            String region_3depth_h_name,
            String b_code,
            String h_code,
            String mountain_yn,
            String main_address_no,
            String sub_address_no,
            String x,
            String y
    ) {}

    public record RoadAddress(
            String address_name,
            String region_1depth_name,
            String region_2depth_name,
            String region_3depth_name,
            String road_name,
            String underground_yn,
            String main_building_no,
            String sub_building_no,
            String building_name,
            String zone_no,
            String x,
            String y
    ) {}
}
