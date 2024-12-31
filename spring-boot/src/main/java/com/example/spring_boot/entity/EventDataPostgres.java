package com.example.spring_boot.entity;

import jakarta.persistence.*;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(
        name = "event-data",
        indexes = @Index(name = "idx_device_id", columnList = "timestamp,deviceId")
)
@Entity
@Data
public class EventDataPostgres implements EventDataInterface {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(name = "device_id")
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
    @Column(name = "battery_level")
    private short batteryLevel;
    @Column(name = "satellite_count")
    private byte satelliteCount;
    @Column(name = "sensor_data")
    private String sensorData;

}
