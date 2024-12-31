package com.example.spring_boot.service;

import com.example.spring_boot.models.CoordinateDTO;
import com.example.spring_boot.repository.EventDataMongoRepository;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class EventDataTransactionService {

    private final EventDataPostgresRepository eventDataRepository;
    private final EventDataMongoRepository eventDataMongoRepository;

    public EventDataTransactionService(EventDataPostgresRepository eventDataRepository, EventDataMongoRepository eventDataMongoRepository) {
        this.eventDataRepository = eventDataRepository;
        this.eventDataMongoRepository = eventDataMongoRepository;
    }

    @Transactional(readOnly = true)
    public void processRange(long deviceId, long startTime, long endTime, DistanceAccumulator accumulator) {
        try (Stream<CoordinateDTO> events = eventDataRepository.findByDeviceIdAndTimestampBetween(deviceId, startTime, endTime)) {
            events.sorted(Comparator.comparingLong(CoordinateDTO::timestamp))
                    .forEach(accumulator::accumulate);
        }
    }

    @Transactional(readOnly = true)
    public void processMongoRange(long deviceId, long startTime, long endTime, DistanceAccumulator accumulator) {
        try (Stream<CoordinateDTO> events = eventDataMongoRepository.findByDeviceIdAndTimestampBetween(deviceId, startTime, endTime)) {
            events.sorted(Comparator.comparingLong(CoordinateDTO::timestamp))
                    .forEach(accumulator::accumulate);
        }
    }
}

