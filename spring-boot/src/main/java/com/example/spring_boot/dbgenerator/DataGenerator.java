package com.example.spring_boot.dbgenerator;

import com.example.spring_boot.entity.IEventData;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataGenerator {

    private static int device_count = 10;
    private static int secondsInDay = 86400;
    private static int days = 3365;

    public void generate(Class<? extends IEventData> clazz, ListCrudRepository rep) {
        var startDate = LocalDateTime.of(2013, 1, 1, 0, 0);
        long startTime = System.currentTimeMillis();
        for (short i = 1; i <= device_count; i++) {
            for (int j = 0; j < days; j++) {
                List<IEventData> list = new ArrayList<>(200);
                for (int k = 0; k < secondsInDay; k += 20) {
                    var eventData = createEventInstance(clazz);

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
                        rep.saveAll(list);
                        //consumer.accept(list);
                        list = new ArrayList<>();
                    }

                }
                if (!list.isEmpty()) {
                    rep.saveAll(list);
                }
                System.out.println("next Day = " + j);
            }
            System.out.println("next device = " + i);
        }
        System.out.println("finished in " + ((System.currentTimeMillis() - startTime) / 1000) + " s");
    }

    private IEventData createEventInstance(Class<? extends IEventData> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


}
