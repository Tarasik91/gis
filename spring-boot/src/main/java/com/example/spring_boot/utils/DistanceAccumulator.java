package com.example.spring_boot.utils;

import com.example.spring_boot.models.CoordinateDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DistanceAccumulator {
    private double totalDistance = 0;
    private CoordinateDTO previousPoint = null;


    public synchronized void accumulate(CoordinateDTO currentPoint) {
        if (previousPoint != null) {
            totalDistance += calculateDistance(
                    previousPoint.latitude(),
                    previousPoint.longitude(),
                    currentPoint.latitude(),
                    currentPoint.longitude()
            );
        }

        previousPoint = currentPoint;
    }

    public synchronized Map<String,Double> getTotalDistance() {
        return Map.of(getStringDate(), totalDistance);
    }


    private String getStringDate() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return  dateFormat.format(new Date(previousPoint.timestamp()));
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2)); // Спрощена формула
    }
}
