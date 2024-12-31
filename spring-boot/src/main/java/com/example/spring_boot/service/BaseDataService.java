package com.example.spring_boot.service;

import com.example.spring_boot.utils.DistanceAccumulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class BaseDataService {

    private final EventDataTransactionService transactionService;

    protected BaseDataService(EventDataTransactionService transactionService) {
        this.transactionService = transactionService;
    }


    protected List<Long[]> splitTimeRange(long start, long end, long interval) {
        List<Long[]> ranges = new ArrayList<>();
        for (long i = start; i < end; i += interval) {
            long rangeEnd = Math.min(i + interval, end); // Виключає накладання
            ranges.add(new Long[]{i, rangeEnd});
        }
        return ranges;
    }

    public Object searchDistance(
            long deviceId, long startTime, long endTime, boolean isDaily,
            int page, int size, boolean isMongo) {

        long interval = 86_400_000L;
        List<Long[]> timeRanges = splitTimeRange(startTime, endTime, interval);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, timeRanges.size());

        List<Long[]> paginatedTimeRanges = isDaily ? timeRanges.subList(fromIndex, toIndex) : timeRanges;
        List<Map<String, Double>> partialDistances = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = paginatedTimeRanges.stream()
                .map(range -> CompletableFuture.runAsync(() -> {
                            DistanceAccumulator accumulator = new DistanceAccumulator();
                            if (isMongo) {
                                transactionService.processMongoRange(deviceId, range[0], range[1], accumulator);
                            } else {
                                transactionService.processRange(deviceId, range[0], range[1], accumulator);
                            }
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

        partialDistances.sort((map1, map2) -> {
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
