package com.example.eventdata_service.service;

import com.example.eventdata_service.repository.MongoEventDataRepository;
import com.example.eventdata_service.dbgenerator.DataGenerator;
import com.example.eventdata_service.dto.DeviceDistanceResponse;
import com.example.eventdata_service.dto.EventDataPayload;
import com.example.eventdata_service.dto.EventDataResponse;
import com.example.eventdata_service.entity.MongoEventData;
import com.example.eventdata_service.utils.DistanceCalculator;
import com.example.eventdata_service.utils.MapperUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MongoEventDataService extends EventDataService {
    @Value("${data.generator}")
    private List<String> generatorList;
    private final MongoEventDataRepository eventDataMongoRepository;
    private final DataGenerator dataGenerator;
    private final DistanceCalculator distanceCalculator;

    MongoEventDataService(
            MongoEventDataRepository repository,
            DataGenerator dataGenerator,
            DistanceCalculator distanceCalculator,
            EventDataPartialTransactionService eventDataPartialTransactionService) {
        super(eventDataPartialTransactionService);
        this.eventDataMongoRepository = repository;
        this.dataGenerator = dataGenerator;
        this.distanceCalculator = distanceCalculator;
    }

    @PostConstruct
    public void init() {
        if (generatorList.contains("mongo")) {
            dataGenerator.generate(MongoEventData.class, eventDataMongoRepository);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public DeviceDistanceResponse octopusDistanceSearch(EventDataPayload payload) {
        return super.octopusDistanceSearch(payload);
    }

    @Override
    public String getDbName() {
        return "mongo";
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventDataResponse> getEvents(EventDataPayload payload) {
        List<MongoEventData> events = eventDataMongoRepository.findByDeviceIdAndTimestampBetween(
                payload.deviceId(), payload.startTime(), payload.endTime(), PageRequest.of(payload.page(), payload.size()));
        return events
                .stream()
                .map(MapperUtils::map2Response)
                .toList();
    }


}
