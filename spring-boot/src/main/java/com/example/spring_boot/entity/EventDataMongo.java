package com.example.spring_boot.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_data")
@Data
public class EventDataMongo implements EventDataInterface {

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
}
