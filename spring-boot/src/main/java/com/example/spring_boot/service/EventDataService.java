package com.example.spring_boot.service;

import com.example.spring_boot.entity.IEventData;
import jakarta.annotation.PostConstruct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public class EventDataService<T extends IEventData, M extends CrudRepository> {

    private M repository;

    private Class<T> modelClass;

    EventDataService(M repository, Class<T> modelClass) {
        this.repository = repository;
        this.modelClass = modelClass;
    }

    @PostConstruct
    public void init() {
        //   createData();
    }



    protected double calculateTotalDistance(List<EventDataRecord> events) {
        double totalDistance = 0.0;

        for (int i = 0; i < events.size() - 1; i++) {
            EventDataRecord current = events.get(i);
            EventDataRecord next = events.get(i + 1);

            totalDistance += calculateDistance(
                    current.latitude(), current.longitude(),
                    next.latitude(), next.longitude()
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

    /*public void createData() {
        var startDate = LocalDateTime.of(2013, 1, 1, 0, 0);
        long startTime = System.currentTimeMillis();
        for (short i = 1; i <= device_count; i++) {
            for (int j = 0; j < days; j++) {
                List<T> list = new ArrayList<>(200);
                for (int k = 0; k < secondsInDay; k += 20) {
                    var eventData = createEventInstance();

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
                        repository.saveAll(list);
                        list = new ArrayList<>();
                    }

                }
                if (!list.isEmpty()) {
                    repository.saveAll(list);
                }
                System.out.println("next Day = " + j);
            }
            System.out.println("next device = " + i);
        }
        System.out.println("finished in " + ((System.currentTimeMillis() - startTime) / 1000) + " s");
    }
*/

}
