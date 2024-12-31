package com.example.spring_boot.service;

import com.example.spring_boot.dbgenerator.DataGenerator;
import com.example.spring_boot.dto.EventDataResponse;
import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.models.EventDataRecord;
import com.example.spring_boot.repository.EventDataMongoRepository;
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
public class EventDataMongoService {
    @Value("${data.generator}")
    private List<String> generatorList;
    private final EventDataMongoRepository eventDataMongoRepository;
    private final DataGenerator dataGenerator;
    private final DistanceCalculator distanceCalculator;

    EventDataMongoService(EventDataMongoRepository repository, DataGenerator dataGenerator, DistanceCalculator distanceCalculator) {
        this.eventDataMongoRepository = repository;
        this.dataGenerator = dataGenerator;
        this.distanceCalculator = distanceCalculator;
    }

    @PostConstruct
    public void init() {
        if (generatorList.contains("mongo")) {
            dataGenerator.generate(EventDataMongo.class, eventDataMongoRepository);
        }
    }

    @Transactional(readOnly = true)
    public Object searchDistance(long deviceId,
                                 long startTime,
                                 long endTime,
                                 boolean isDaily,
                                 int page,
                                 int size,
                                 boolean isMongo) {
        // return super.searchDistance(deviceId, startTime, endTime, isDaily, page, size, isMongo);
        return null;
    }

    @Transactional(readOnly = true)
    public List<EventDataResponse> getEvents(long deviceId,
                                                  long startTime,
                                                  long endTime,
                                                  Pageable pageable) {
        List<EventDataMongo> events = eventDataMongoRepository.findByDeviceIdAndTimestampBetween(
                deviceId, startTime, endTime, pageable);
        return events
                .stream()
                .map(MapperUtils::map2Response)
                .toList();
    }
    @Transactional
    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
        Stream<EventDataRecord> events = eventDataMongoRepository.findByDeviceIdAndTimestampBetween(
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
