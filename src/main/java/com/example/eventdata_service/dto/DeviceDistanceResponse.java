package com.example.eventdata_service.dto;

import java.util.ArrayList;
import java.util.List;

public record DeviceDistanceResponse(Double distance, List<Double> dailyDistances) {

    public DeviceDistanceResponse(double distance) {
        this(distance,null);
    }

    public DeviceDistanceResponse(List<Double> dailyDistances) {
        this(null,dailyDistances);
    }

}
