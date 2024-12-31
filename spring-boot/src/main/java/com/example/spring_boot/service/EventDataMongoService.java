package com.example.spring_boot.service;

import com.example.spring_boot.dbgenerator.DataGenerator;
import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.repository.EventDataMongoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventDataMongoService extends BaseDataService {
    @Value("${data.generator}")
    private List<String> generatorList;
    private final EventDataMongoRepository eventDataMongoRepository;
    private final DataGenerator dataGenerator;

    EventDataMongoService(EventDataMongoRepository repository,
                          DataGenerator dataGenerator,
                          EventDataTransactionService transactionService) {
        super(transactionService);
        this.eventDataMongoRepository = repository;
        this.dataGenerator = dataGenerator;
    }

    @PostConstruct
    public void init() {
        if (generatorList.contains("mongo")) {
            dataGenerator.generate(EventDataMongo.class, eventDataMongoRepository);
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
