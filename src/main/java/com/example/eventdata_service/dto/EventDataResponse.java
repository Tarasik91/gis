package com.example.eventdata_service.dto;

public class EventDataResponse {
    private String date;
    private double latitude;
    private double longitude;
    private int sateliteCount;
    private String sensorData;

    public EventDataResponse() {}

    public EventDataResponse(String date,
                             double latitude,
                             double longitude,
                             int sateliteCount,
                             String sensorData) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sateliteCount = sateliteCount;
        this.sensorData = sensorData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSateliteCount() {
        return sateliteCount;
    }

    public void setSateliteCount(int sateliteCount) {
        this.sateliteCount = sateliteCount;
    }

    public String getSensorData() {
        return sensorData;
    }

    public void setSensorData(String sensorData) {
        this.sensorData = sensorData;
    }
}
