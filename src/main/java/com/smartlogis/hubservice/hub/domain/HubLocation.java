package com.smartlogis.hubservice.hub.domain;

import com.smartlogis.hubservice.hub.domain.exception.HubAddressInvalidException;
import com.smartlogis.hubservice.hub.domain.exception.HubInvalidLocationException;
import com.smartlogis.hubservice.hub.domain.exception.HubMessageCode;
import com.smartlogis.hubservice.hub.infrastructure.type.PostgresVectorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubLocation {

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "location_vec", columnDefinition = "vector(2)")
    @JdbcType(PostgresVectorType.class)
    private float[] locationVec;

    public HubLocation(String address, double latitude, double longitude) {
        validate(address, latitude, longitude);

        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationVec = new float[]{
                (float) latitude,
                (float) longitude
        };
    }

    private void validate(String address, double latitude, double longitude) {
        validateAddress(address);
        validateLatitude(latitude);
        validateLongitude(longitude);
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new HubAddressInvalidException(HubMessageCode.HUB_ADDRESS_INVALID);
        }
    }

    private void validateLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new HubInvalidLocationException(HubMessageCode.HUB_INVALID_LOCATION);
        }
    }

    private void validateLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new HubInvalidLocationException(HubMessageCode.HUB_INVALID_LOCATION);
        }
    }
}
