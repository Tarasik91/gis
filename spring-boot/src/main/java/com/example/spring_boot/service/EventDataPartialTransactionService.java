package com.example.spring_boot.service;

import com.example.spring_boot.adapters.RepoAdapter;
import com.example.spring_boot.models.EventDataRecord;
import com.example.spring_boot.repository.MongoEventDataRepository;
import com.example.spring_boot.repository.PostgresEventDataRepository;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class EventDataPartialTransactionService {

    @Transactional(readOnly = true)
    public void processRange(RepoAdapter adapter, long deviceId, long startTime, long endTime, DistanceAccumulator accumulator) {
        try (Stream<EventDataRecord> events = adapter.processPartition(deviceId, startTime, endTime)) {
            events.sorted(Comparator.comparingLong(EventDataRecord::timestamp))
                    .forEach(accumulator::accumulate);
        }
    }
}

