package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.models.CoordinateDTO;
import com.example.spring_boot.repository.EventDataMongoRepository;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EventDataMongoService extends EventDataService<EventDataMongo,EventDataMongoRepository>{

    EventDataTransactionService transactionService;


    EventDataMongoService(EventDataMongoRepository repository, EventDataTransactionService transactionService) {
        super(repository, EventDataMongo.class);
        this.transactionService = transactionService;
    }

    @Transactional(readOnly = true)
    public List<Object> searchDistance(long deviceId, long startTime, long endTime, boolean isDaily) {
        System.out.println("Mongo Start!!!");

        long interval = 86_400_000L; // Наприклад, 1 година (в мілісекундах)
        List<Long[]> timeRanges = splitTimeRange(startTime, endTime, interval);
        List<Map> partialDistances = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = timeRanges.stream()
                .map(range -> CompletableFuture.runAsync(() -> {
                            DistanceAccumulator accumulator = new DistanceAccumulator();
                            transactionService.processMongoRange(deviceId, range[0], range[1], accumulator);
                            partialDistances.add(accumulator.getTotalDistance());
                        }
                ))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        double totalDistance = 0;
                //partialDistances.stream().mapToDouble(it->it.values().iterator().next()).sum();

        return List.of(Map.of("totalDistance", totalDistance));
    }
}
