package com.example.spring_boot.service;

import com.example.spring_boot.adapters.RepoAdapter;
import com.example.spring_boot.dbgenerator.DataGenerator;
import com.example.spring_boot.dto.EventDataResponse;
import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.models.EventDataRecord;
import com.example.spring_boot.repository.PostgresEventDataRepository;
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
            dataGenerator.generate(EventDataPostgres.class, eventDataRepository);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Object octopusDistanceSearch(RepoAdapter adapter,
                                        long deviceId,
                                        long startTime,
                                        long endTime,
                                        boolean isDaily,
                                        int page,
                                        int size) {
        return super.octopusDistanceSearch(adapter, deviceId, startTime, endTime, isDaily, page, size);
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
