package com.example.spring_boot.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_data")
@Data
@CompoundIndexes({
        @CompoundIndex(name = "device_timestamp", def = "{'deviceId' : 1, 'timestamp': 1,'latitude' : 1, 'longitude': 1}")
})
public class EventDataMongo implements IEventData {

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
