package com.example.spring_boot.service;

import com.example.spring_boot.adapters.MongoRepoAdapter;
import com.example.spring_boot.adapters.PostgresRepoAdapter;
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

    public EventDataPartialTransactionService(MongoRepoAdapter mongoRepoAdapter, PostgresRepoAdapter postgresRepoAdapter) {
        this.adapters = new ArrayList<>();
        adapters.add(mongoRepoAdapter);
        adapters.add(postgresRepoAdapter);
    }

    @Transactional(readOnly = true)
    public void processRange(String db, long deviceId, long startTime, long endTime, DistanceAccumulator accumulator) {
        adapters.stream().filter(it->it.getDbName().equalsIgnoreCase(db))
                .findFirst().ifPresent(adapter->{
            try (Stream<EventDataRecord> events = adapter.processPartition(deviceId, startTime, endTime)) {
                events.sorted(Comparator.comparingLong(EventDataRecord::timestamp))
                        .forEach(accumulator::accumulate);
            }
        });
    }
}

