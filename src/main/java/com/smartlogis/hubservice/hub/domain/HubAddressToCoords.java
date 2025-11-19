package com.smartlogis.hubservice.hub.domain;

import java.util.List;


public interface HubAddressToCoords {
    List<Double> convert(String address);
}
