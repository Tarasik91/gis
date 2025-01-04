package com.example.spring_boot.service;

import com.example.spring_boot.adapters.RepoAdapter;
import com.example.spring_boot.dbgenerator.DataGenerator;
import com.example.spring_boot.dto.DeviceDistanceResponse;
import com.example.spring_boot.dto.EventDataPayload;
import com.example.spring_boot.dto.EventDataResponse;
import com.example.spring_boot.entity.PostgresEventData;
import com.example.spring_boot.repository.PostgresEventDataRepository;
import com.example.spring_boot.utils.DistanceCalculator;
import com.example.spring_boot.utils.MapperUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
