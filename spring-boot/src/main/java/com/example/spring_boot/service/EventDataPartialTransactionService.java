package com.example.spring_boot.service;
import com.example.spring_boot.adapters.RepoAdapter;
import com.example.spring_boot.models.EventDataRecord;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class EventDataPartialTransactionService {

    private final List<RepoAdapter> adapters;

    public EventDataPartialTransactionService(List<RepoAdapter> adapters) {
        this.adapters = new ArrayList<>();
        this.adapters.addAll(adapters);
    }

    @Transactional(readOnly = true)
    public DistanceAccumulator processRange(String db, long deviceId, long startTime, long endTime) {
        DistanceAccumulator accumulator = new DistanceAccumulator();
        adapters.stream().filter(it->it.getDbName().equalsIgnoreCase(db))
                .findFirst().ifPresent(adapter->{
            try (Stream<EventDataRecord> events = adapter.processPartition(deviceId, startTime, endTime)) {
                events.sorted(Comparator.comparingLong(EventDataRecord::timestamp))
                        .forEach(accumulator::accumulate);
            }
        });
        return accumulator;
    }
}

