package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class EventDataPostgresService {
    @Value("${data.generator}")
    private List<String> generatorList;

    private final DataGenerator dataGenerator;

    private final DistanceCalculator distanceCalculator;
    private final EventDataPostgresRepository eventDataRepository;

    EventDataPostgresService(EventDataPostgresRepository repository, DataGenerator dataGenerator, DistanceCalculator distanceCalculator) {
        this.eventDataRepository = repository;
        this.dataGenerator = dataGenerator;
        this.distanceCalculator = distanceCalculator;
    }

    @PostConstruct
    public void init() {
        if (generatorList.contains("postgres")) {
            dataGenerator.generate(EventDataPostgres.class, eventDataRepository);
        }
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
        double totalDistance = distanceCalculator.calculateTotalDistance(events.toList());
        result.add(Map.of("totalDistance", totalDistance));
        return result;
    }

}
