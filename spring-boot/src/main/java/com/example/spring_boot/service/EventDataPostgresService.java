package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.models.CoordinateDTO;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class EventDataPostgresService extends EventDataService<EventDataPostgres,EventDataPostgresRepository>{

    private EventDataPostgresRepository eventDataRepository;

    EventDataPostgresService(EventDataPostgresRepository repository) {
        super(repository, EventDataPostgres.class);
        this.eventDataRepository = repository;
    }

    @Transactional(readOnly = true)
    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
        DistanceAccumulator accumulator = new DistanceAccumulator();
        try (Stream<CoordinateDTO> events = eventDataRepository.findByDeviceIdAndTimestampBetween(deviceId, startTime, endTime)) {
            events.sorted(Comparator.comparingLong(CoordinateDTO::timestamp))
                    .forEach(accumulator::accumulate);

            return List.of(Map.of("totalDistance", accumulator.getTotalDistance()));
        }
    }



}
