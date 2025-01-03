package com.example.spring_boot.service;

import com.example.spring_boot.adapters.RepoAdapter;
import com.example.spring_boot.dbgenerator.DataGenerator;
import com.example.spring_boot.dto.EventDataResponse;
import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.models.EventDataRecord;
import com.example.spring_boot.repository.MongoEventDataRepository;
import com.example.spring_boot.utils.DistanceCalculator;
import com.example.spring_boot.utils.MapperUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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
            dataGenerator.generate(EventDataMongo.class, eventDataMongoRepository);
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
        List<EventDataMongo> events = eventDataMongoRepository.findByDeviceIdAndTimestampBetween(
                deviceId, startTime, endTime, pageable);
        return events
                .stream()
                .map(MapperUtils::map2Response)
                .toList();
    }


}
