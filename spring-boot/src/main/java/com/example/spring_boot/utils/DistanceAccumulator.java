package com.example.spring_boot.utils;

import com.example.spring_boot.models.EventDataRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class DistanceAccumulator {
    private double totalDistance = 0;

    //TODO do we need to store a state? syncronization method will make the app more slower
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

    public synchronized double getTotalDistance() {
        return totalDistance;
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
