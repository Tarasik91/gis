package com.example.eventdata_service.service;

import com.example.eventdata_service.dbgenerator.DataGenerator;
import com.example.eventdata_service.dto.DeviceDistanceResponse;
import com.example.eventdata_service.dto.EventDataPayload;
import com.example.eventdata_service.dto.EventDataResponse;
import com.example.eventdata_service.entity.PostgresEventData;
import com.example.eventdata_service.repository.PostgresEventDataRepository;
import com.example.eventdata_service.utils.DistanceCalculator;
import com.example.eventdata_service.utils.MapperUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostgresEventDataService extends EventDataService {
    @Value("${data.generator}")
    private List<String> generatorList;

    private final DataGenerator dataGenerator;

    private final DistanceCalculator distanceCalculator;

    private final PostgresEventDataRepository eventDataRepository;

    PostgresEventDataService(PostgresEventDataRepository repository,
                             DataGenerator dataGenerator,
                             DistanceCalculator distanceCalculator,
                             EventDataPartialTransactionService eventDataPartialTransactionService) {
        super(eventDataPartialTransactionService);
        this.eventDataRepository = repository;
        this.dataGenerator = dataGenerator;
        this.distanceCalculator = distanceCalculator;
    }

    @PostConstruct
    public void init() {
        if (generatorList.contains("postgres")) {
            dataGenerator.generate(PostgresEventData.class, eventDataRepository);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public DeviceDistanceResponse octopusDistanceSearch(EventDataPayload payload) {
        return super.octopusDistanceSearch(payload);
    }

    @Override
    public String getDbName() {
        return "postgres";
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventDataResponse> getEvents(EventDataPayload payload) {
        List<PostgresEventData> events = eventDataRepository.findByDeviceIdAndTimestampBetween(
                payload.deviceId(), payload.startTime(), payload.endTime(), PageRequest.of(payload.page(), payload.size()));
        return events
                .stream()
                .map(MapperUtils::map2Response)
                .toList();
    }

}
