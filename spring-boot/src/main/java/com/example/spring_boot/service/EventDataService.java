package com.example.spring_boot.service;

import com.example.spring_boot.adapters.RepoAdapter;
import com.example.spring_boot.dto.DeviceDistanceResponse;
import com.example.spring_boot.dto.EventDataPayload;
import com.example.spring_boot.dto.EventDataResponse;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class EventDataService {

    private final EventDataPartialTransactionService transactionService;

    protected EventDataService(EventDataPartialTransactionService transactionService) {
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

    abstract public String getDbName();

    abstract public List<EventDataResponse> getEvents(EventDataPayload payload);


    public DeviceDistanceResponse octopusDistanceSearch(EventDataPayload payload) {

        long interval = 86_400_000L;
        List<Long[]> timeRanges = splitTimeRange(payload.startTime(), payload.endTime(), interval);

        int fromIndex = payload.page() * payload.size();
        int toIndex = Math.min(fromIndex + payload.size(), timeRanges.size());

        List<Long[]> paginatedTimeRanges = payload.isDaily() ? timeRanges.subList(fromIndex, toIndex) : timeRanges;
        List<Double> partialDistances = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = paginatedTimeRanges.stream()
                .map(range -> CompletableFuture.runAsync(() -> {
                            DistanceAccumulator accumulator = transactionService.processRange(payload.db(), payload.deviceId(), range[0], range[1]);
                            partialDistances.add(accumulator.getTotalDistance());
                        }
                ))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        if (!payload.isDaily()) {
            double totalDistance = partialDistances.stream().mapToDouble(it -> it).sum();
            return new DeviceDistanceResponse(totalDistance);
        }


        return new DeviceDistanceResponse(partialDistances);
    }

}
