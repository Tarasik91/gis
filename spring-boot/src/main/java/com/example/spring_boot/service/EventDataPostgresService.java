package com.example.spring_boot.service;

import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import com.example.spring_boot.utils.DistanceAccumulator;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EventDataPostgresService extends EventDataService<EventDataPostgres, EventDataPostgresRepository> {

    private final EntityManager entityManager;
    private final EventDataTransactionService transactionService;

    EventDataPostgresService(EventDataPostgresRepository repository, EntityManager entityManager, EventDataTransactionService transactionService) {
        super(repository, EventDataPostgres.class);
        this.entityManager = entityManager;
        this.transactionService = transactionService;
    }

    @Transactional(readOnly = true)
    public Object searchDistance(
            long deviceId, long startTime, long endTime, boolean isDaily,
            int page, int size) {

        long interval = 86_400_000L; // 1 день
        List<Long[]> timeRanges = splitTimeRange(startTime, endTime, interval);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, timeRanges.size());
        if (fromIndex >= timeRanges.size()) {
            return List.of();
        }

        List<Long[]> paginatedTimeRanges = isDaily ? timeRanges.subList(fromIndex, toIndex) : timeRanges;
        List<Map<String, Double>> partialDistances = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = paginatedTimeRanges.stream()
                .map(range -> CompletableFuture.runAsync(() -> {
                            DistanceAccumulator accumulator = new DistanceAccumulator();
                            transactionService.processRange(deviceId, range[0], range[1], accumulator);
                            partialDistances.add(accumulator.getTotalDistance());
                        }
                ))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        if (!isDaily) {
            double totalDistance = partialDistances.stream().mapToDouble(it -> it.values().iterator().next()).sum();
            return Map.of(
                    "totalDistance", totalDistance
            );
        }

        partialDistances.sort((map1,map2) -> {
            String date1 = map1.keySet().iterator().next();
            String date2 = map2.keySet().iterator().next();
            return date1.compareTo(date2);
        });
        return Map.of(
                "page", page,
                "size", size,
                "partialDistances", partialDistances
        );
    }

}
