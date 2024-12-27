package com.example.spring_boot.utils;

import com.example.spring_boot.models.CoordinateDTO;

public class DistanceAccumulator {
    private double totalDistance = 0;
    private CoordinateDTO previousPoint = null;

    public DistanceAccumulator accumulate(CoordinateDTO currentPoint) {
        if (previousPoint != null) {
            totalDistance += calculateDistance(
                    previousPoint.latitude(),
                    previousPoint.longitude(),
                    currentPoint.latitude(),
                    currentPoint.longitude()
            );
        }
        previousPoint = currentPoint;
        return this;
    }

    public DistanceAccumulator combine(DistanceAccumulator other) {
        this.totalDistance += other.totalDistance;
        return this;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Реалізуйте вашу формулу Haversine або іншу для розрахунку відстані
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2)); // Спрощена формула
    }
}
