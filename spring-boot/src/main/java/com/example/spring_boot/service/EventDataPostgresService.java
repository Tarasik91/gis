package com.example.spring_boot.service;

import com.example.spring_boot.dbgenerator.DataGenerator;
import com.example.spring_boot.dto.EventDataResponse;
import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.models.EventDataRecord;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import com.example.spring_boot.utils.DistanceCalculator;
import com.example.spring_boot.utils.MapperUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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

    EventDataPostgresService(EventDataPostgresRepository repository,
                             DataGenerator dataGenerator,
                             DistanceCalculator distanceCalculator) {
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
    //TODO should return model instead of object
    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
        Stream<EventDataRecord> events = eventDataRepository.findByDeviceIdAndTimestampBetween(
                deviceId, startTime, endTime
        );
        List<Object> result = new ArrayList<>();
        double totalDistance = distanceCalculator.calculateTotalDistance(events.toList());
        result.add(Map.of("totalDistance", totalDistance));
        return result;
    }

    @Transactional(readOnly = true)
    public List<EventDataResponse> getEvents(long deviceId,
                                             long startTime,
                                             long endTime,
                                             Pageable pageable) {
        List<EventDataPostgres> events = eventDataRepository.findByDeviceIdAndTimestampBetween(
                deviceId, startTime, endTime, pageable);
        return events
                .stream()
                .map(MapperUtils::map2Response)
                .toList();
    }

}
