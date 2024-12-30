package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.repository.EventDataPostgresRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class EventDataPostgresService extends EventDataService<EventDataPostgres,EventDataPostgresRepository>{

    private EventDataPostgresRepository eventDataRepository;

    EventDataPostgresService(EventDataPostgresRepository repository) {
        super(repository, EventDataPostgres.class);
        this.eventDataRepository = repository;

        //insertData();
    }

    public void insertData(){
        new DataGenerator().generate(EventDataMongo.class, eventDataRepository);
    }


    @Transactional(readOnly = true)
    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
        Stream<EventDataRecord> events = eventDataRepository.findByDeviceIdAndTimestampBetween(
                deviceId, startTime, endTime
        );
       /* if (events.isEmpty()) {
            return List.of();
        }*/
        List<Object> result = new ArrayList<>();
        double totalDistance = calculateTotalDistance(events.toList());
        result.add(Map.of("totalDistance", totalDistance));
        return result;
    }

}
