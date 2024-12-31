package com.example.spring_boot.service;

import com.example.spring_boot.dbgenerator.DataGenerator;
import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventDataPostgresService extends BaseDataService {
    @Value("${data.generator}")
    private List<String> generatorList;

    private final DataGenerator dataGenerator;

    private final EventDataPostgresRepository eventDataRepository;

    EventDataPostgresService(EventDataPostgresRepository repository,
                             DataGenerator dataGenerator,
                             EventDataTransactionService transactionService) {
        super(transactionService);
        this.eventDataRepository = repository;
        this.dataGenerator = dataGenerator;
    }

    @PostConstruct
    public void init() {
        if (generatorList.contains("postgres")) {
            dataGenerator.generate(EventDataPostgres.class, eventDataRepository);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Object searchDistance(long deviceId,
                                 long startTime,
                                 long endTime,
                                 boolean isDaily,
                                 int page,
                                 int size,
                                 boolean isMongo) {
        return super.searchDistance(deviceId, startTime, endTime, isDaily, page, size, isMongo);
    }

}
