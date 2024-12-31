package com.example.spring_boot.utils;

import com.example.spring_boot.models.EventDataRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DistanceAccumulator {
    private double totalDistance = 0;
    private EventDataRecord previousPoint = null;


    public synchronized void accumulate(EventDataRecord currentPoint) {
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
        final double R = 6371;
        double p = Math.PI / 180;

        double a = 0.5 - Math.cos((lat2 - lat1) * p) / 2
                + Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                (1 - Math.cos((lon2 - lon1) * p)) / 2;

        return 2 * R * Math.asin(Math.sqrt(a));
    }

}
