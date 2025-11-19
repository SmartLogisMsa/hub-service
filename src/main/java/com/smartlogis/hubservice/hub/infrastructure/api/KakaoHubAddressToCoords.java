package com.smartlogis.hubservice.hub.infrastructure.api;

import com.smartlogis.hubservice.hub.domain.HubAddressToCoords;
import com.smartlogis.hubservice.hub.domain.exception.HubCoordinateNotFoundException;
import com.smartlogis.hubservice.hub.domain.exception.HubMessageCode;
import com.smartlogis.hubservice.hub.infrastructure.api.dto.KakaoGeocodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ResponseStatus
@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoHubAddressToCoords implements HubAddressToCoords {

    private final RestTemplate kakaoRestTemplate;

    @Value("${kakao.rest-api-key}")
    private String restApiKey;

    @Override
    public List<Double> convert(String address) {

        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + restApiKey);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoGeocodeResponse> response =
                    kakaoRestTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            KakaoGeocodeResponse.class
                    );

            KakaoGeocodeResponse body = response.getBody();

            if (body == null || body.documents() == null || body.documents().isEmpty()) {
                throw new HubCoordinateNotFoundException(
                        HubMessageCode.HUB_COORDINATE_NOT_FOUND,
                        address
                );
            }

            KakaoGeocodeResponse.Document first = body.documents().getFirst();

            double latitude = Double.parseDouble(first.y());
            double longitude = Double.parseDouble(first.x());

            return List.of(latitude, longitude);

        }   catch (Exception e) {
            log.error("[Kakao API 오류] {}", e.getMessage());
            throw e;
        }
    }
}
