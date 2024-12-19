package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventData;
import com.example.spring_boot.repository.EventDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class EventDataService {

    private EventDataRepository eventDataRepository;
    private static int user_count = 10;

    public EventDataService(EventDataRepository repository) {
        this.eventDataRepository = repository;
    }

    @PostConstruct
    public void init() {
        createData();
    }

    public void createData() {
        var startDate = LocalDateTime.of(2024, 1, 1,0,0);
        for (short i = 1; i <= user_count; i++) {
            for (int j = 0; j < 350; j++) {
                for (int k = 0; k < 86400; k = +20) {
                    var eventData = new EventData();

                    eventData.setDeviceId(i);
                    short al = (short) (12 + i);
                    eventData.setAltitude(al);
                    eventData.setSensorData("{sen" + i + "=" + i + "}");
                    eventData.setHeading(al);
                    eventData.setBatteryLevel((short)1);
                    LocalDateTime date = startDate.plusDays(j).plusSeconds(k);
                    eventData.setTimestamp( Timestamp.valueOf(date).getNanos());
                    eventData.setSatelliteCount((byte)i);
                    var lat =ThreadLocalRandom.current().nextInt(1000);
                    var longit =ThreadLocalRandom.current().nextInt(10000);
                    eventData.setLatitude(Double.parseDouble("49.842" + lat));
                    eventData.setLongitude(Double.parseDouble("24.03" + longit));
                    eventDataRepository.save(eventData);
                }
                System.out.println("next Day = " + j);
            }
            System.out.println("next device = " + i);
        }
        System.out.println("finish");
    }
}
