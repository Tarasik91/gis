package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.repository.EventDataMongoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EventDataMongoService extends EventDataService<EventDataMongo,EventDataMongoRepository>{
    private EventDataMongoRepository eventDataMongoRepository;

    EventDataMongoService(EventDataMongoRepository repository) {
        super(repository, EventDataMongo.class);
        this.eventDataMongoRepository = repository;
    }


    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily, Pageable pageable) {
        List<EventDataMongo> events = eventDataMongoRepository.findByDeviceIdAndTimestampBetween(
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
}
