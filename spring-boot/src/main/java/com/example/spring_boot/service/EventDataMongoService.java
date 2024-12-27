package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.models.CoordinateDTO;
import com.example.spring_boot.repository.EventDataMongoRepository;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class EventDataMongoService extends EventDataService<EventDataMongo,EventDataMongoRepository>{
    private EventDataMongoRepository eventDataMongoRepository;

    EventDataMongoService(EventDataMongoRepository repository) {
        super(repository, EventDataMongo.class);
        this.eventDataMongoRepository = repository;
    }


//    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
//        var startProcessing = System.currentTimeMillis();
//        List<EventDataMongo> events = eventDataMongoRepository.findByDeviceIdAndTimestampBetween(
//                deviceId, startTime, endTime
//        );
//        System.out.println("Finished fetching from Postgres " + ((System.currentTimeMillis() - startProcessing) / 1000) + " s");
//        if (events.isEmpty()) {
//            return List.of();
//        }
//        List<Object> result = new ArrayList<>();
//        double totalDistance = calculateTotalDistance(events);
//        result.add(Map.of("totalDistance", totalDistance));
//        return result;
//    }

    @Transactional(readOnly = true)
    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
        DistanceAccumulator accumulator = new DistanceAccumulator();
        try (Stream<CoordinateDTO> events = eventDataMongoRepository.findByDeviceIdAndTimestampBetween(deviceId, startTime, endTime)) {
            events.sorted(Comparator.comparingLong(CoordinateDTO::timestamp))
                    .forEach(accumulator::accumulate); // Обробляємо кожен об'єкт окремо

            return List.of(Map.of("totalDistance", accumulator.getTotalDistance()));
        }
    }
}
