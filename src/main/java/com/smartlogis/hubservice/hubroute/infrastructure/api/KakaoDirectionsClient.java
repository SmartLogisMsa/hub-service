package com.smartlogis.hubservice.hubroute.infrastructure.api;

import com.smartlogis.hubservice.hubroute.infrastructure.dto.KakaoDirectionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoDirectionsClient {

    private final RestTemplate kakaoRestTemplate;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private static final String URL = "https://apis-navi.kakaomobility.com/v1/directions";

    public KakaoDirectionsResponse getDirections(double startLat, double startLng,
                                                 double endLat, double endLng) {

        String origin = startLng + "," + startLat;
        String destination = endLng + "," + endLat;

        String fullUrl = URL + "?origin=" + origin + "&destination=" + destination;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoDirectionsResponse> response =
                kakaoRestTemplate.exchange(
                        fullUrl,
                        HttpMethod.GET,
                        request,
                        KakaoDirectionsResponse.class
                );

        return response.getBody();
    }
}
