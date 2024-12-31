package com.example.spring_boot.utils;

import com.example.spring_boot.models.EventDataRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistanceCalculator {

    public double calculateTotalDistance(List<EventDataRecord> events) {
        double totalDistance = 0.0;

        for (int i = 0; i < events.size() - 1; i++) {
            EventDataRecord current = events.get(i);
            EventDataRecord next = events.get(i + 1);

            totalDistance += calculateDistance(
                    current.latitude(), current.longitude(),
                    next.latitude(), next.longitude()
            );
        }

        return totalDistance;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // радіус Землі в км
        double p = Math.PI / 180;

        double a = 0.5 - Math.cos((lat2 - lat1) * p) / 2
                + Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                (1 - Math.cos((lon2 - lon1) * p)) / 2;

        return 2 * R * Math.asin(Math.sqrt(a));
    }
}
