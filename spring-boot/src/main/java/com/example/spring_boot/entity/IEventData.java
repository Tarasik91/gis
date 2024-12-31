package com.example.spring_boot.entity;

public interface IEventData {
    double getLatitude();
    double getLongitude();
    long getDeviceId();
    long getTimestamp();
    short getAltitude();
    short getHeading();
    short getSpeed();
    short getBatteryLevel();
    byte getSatelliteCount();
    String getSensorData();

    void setLatitude(double latitude);
    void setLongitude(double longitude);
    void setAltitude(short altitude);
    void setHeading(short heading);
    void setSpeed(short speed);
    void setBatteryLevel(short batteryLevel);
    void setSatelliteCount(byte satelliteCount);
    void setSensorData(String sensorData);
    void setTimestamp(long timestamp);
    void setDeviceId(long deviceId);

}
