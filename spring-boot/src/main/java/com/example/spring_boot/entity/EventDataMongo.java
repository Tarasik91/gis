package com.example.spring_boot.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_data")
public class EventDataMongo {

    @Id
    private String id;
    private long deviceId;
    private long timestamp;
    private double latitude;
    private double longitude;
    private short altitude;
    private short heading;
    private short speed;
    private short batteryLevel;
    private byte satelliteCount;
    private String sensorData;


    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public short getAltitude() {
        return altitude;
    }

    public void setAltitude(short altitude) {
        this.altitude = altitude;
    }

    public short getHeading() {
        return heading;
    }

    public void setHeading(short heading) {
        this.heading = heading;
    }

    public short getSpeed() {
        return speed;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public short getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(short batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public byte getSatelliteCount() {
        return satelliteCount;
    }

    public void setSatelliteCount(byte satelliteCount) {
        this.satelliteCount = satelliteCount;
    }

    public String getSensorData() {
        return sensorData;
    }

    public void setSensorData(String sensorData) {
        this.sensorData = sensorData;
    }
}
