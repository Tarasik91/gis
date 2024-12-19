package com.example.spring_boot.entity;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "event-data")
@Entity
public class EventData {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column
    private long deviceId;
    @Column
    private long timestamp;
    @Column
    private double latitude;
    @Column
    private double longitude;
    @Column
    private short altitude;
    @Column
    private short heading;
    @Column
    private short speed;
    @Column
    private short batteryLevel;
    @Column
    private byte satelliteCount;
    @Column
    private String sensorData;


    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
