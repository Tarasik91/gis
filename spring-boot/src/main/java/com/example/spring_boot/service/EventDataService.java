package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventData;
import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.repository.EventDataMongoRepository;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class EventDataService {

    private EventDataPostgresRepository eventDataRepository;
    private EventDataMongoRepository eventDataMongoRepository;
    private static int device_count = 10;
    private static int secondsInDay = 86400;
    private static int days = 365;

    public EventDataService(EventDataPostgresRepository repository, EventDataMongoRepository eventDataMongoRepository) {
        this.eventDataRepository = repository;
        this.eventDataMongoRepository = eventDataMongoRepository;
    }

    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
        List<EventData> events = eventDataRepository.findByDeviceIdAndTimestampBetween(
                deviceId, startTime, endTime
        );
        if (events.isEmpty()) {
            return List.of();
        }
        List<Object> result = new ArrayList<>();
        double totalDistance = calculateTotalDistance(events);
        result.add(Map.of("totalDistance", totalDistance));
        return result;
    }

    private double calculateTotalDistance(List<EventData> events) {
        double totalDistance = 0.0;

        for (int i = 0; i < events.size() - 1; i++) {
            EventData current = events.get(i);
            EventData next = events.get(i + 1);

            totalDistance += calculateDistance(
                    current.getLatitude(), current.getLongitude(),
                    next.getLatitude(), next.getLongitude()
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

    @PostConstruct
    public void init() {
//        createDataPostgres();
//        createMongoData();
    }

    public void createDataPostgres() {
        var startDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        long startTime = System.currentTimeMillis();
        for (short i = 1; i <= device_count; i++) {
            for (int j = 0; j < days; j++) {
                List<EventData> list = new ArrayList<>(200);
                for (int k = 0; k < secondsInDay; k += 20) {
                    var eventData = new EventData();

                    eventData.setDeviceId(i);
                    short al = (short) (12 + 1);
                    eventData.setAltitude(al);
                    eventData.setSensorData("{sen" + 2 + "=" + 2 + "}");
                    eventData.setHeading(al);
                    eventData.setBatteryLevel((short) 1);
                    LocalDateTime date = startDate.plusDays(j).plusSeconds(k);
                    eventData.setTimestamp(Timestamp.valueOf(date).getTime());
                    eventData.setSatelliteCount((byte) 2);
                    var lat = ThreadLocalRandom.current().nextInt(1000);
                    var longit = ThreadLocalRandom.current().nextInt(10000);
                    eventData.setLatitude(Double.parseDouble("49.842" + lat));
                    eventData.setLongitude(Double.parseDouble("24.03" + longit));
                    list.add(eventData);
                    if (list.size() == 200) {
                        eventDataRepository.saveAll(list);
                        list = new ArrayList<>();
                    }

                }
                if (!list.isEmpty()) {
                    eventDataRepository.saveAll(list);
                }
                System.out.println("next Day = " + j);
            }
            System.out.println("next device = " + i);
        }
        System.out.println("finished in " + ((System.currentTimeMillis() - startTime) / 1000) + " s");
    }

    public void createMongoData() {
        var startDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        long startTime = System.currentTimeMillis();
        for (short i = 1; i <= device_count; i++) {
            for (int j = 0; j < days; j++) {
                List<EventDataMongo> list = new ArrayList<>(200);
                for (int k = 0; k < secondsInDay; k += 20) {
                    var eventData = new EventDataMongo();

                    eventData.setDeviceId(i);
                    short al = (short) (12 + 1);
                    eventData.setAltitude(al);
                    eventData.setSensorData("{sen" + 2 + "=" + 2 + "}");
                    eventData.setHeading(al);
                    eventData.setBatteryLevel((short) 1);
                    LocalDateTime date = startDate.plusDays(j).plusSeconds(k);
                    eventData.setTimestamp(Timestamp.valueOf(date).getTime());
                    eventData.setSatelliteCount((byte) 2);
                    var lat = ThreadLocalRandom.current().nextInt(1000);
                    var longit = ThreadLocalRandom.current().nextInt(10000);
                    eventData.setLatitude(Double.parseDouble("49.842" + lat));
                    eventData.setLongitude(Double.parseDouble("24.03" + longit));
                    list.add(eventData);
                    if (list.size() == 200) {
                        eventDataMongoRepository.saveAll(list);
                        list = new ArrayList<>();
                    }

                }
                if (!list.isEmpty()) {
                    eventDataMongoRepository.saveAll(list);
                }
                System.out.println("next Day = " + j);
            }
            System.out.println("next device = " + i);
        }
        System.out.println("finished in " + ((System.currentTimeMillis() - startTime) / 1000) + " s");
    }
}
