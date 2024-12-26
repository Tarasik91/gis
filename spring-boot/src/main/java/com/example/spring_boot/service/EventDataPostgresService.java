package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EventDataPostgresService extends EventDataService<EventDataPostgres,EventDataPostgresRepository>{

    private EventDataPostgresRepository eventDataRepository;

    EventDataPostgresService(EventDataPostgresRepository repository) {
        super(repository, EventDataPostgres.class);
        this.eventDataRepository = repository;
    }


    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
        List<EventDataPostgres> events = eventDataRepository.findByDeviceIdAndTimestampBetween(
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
