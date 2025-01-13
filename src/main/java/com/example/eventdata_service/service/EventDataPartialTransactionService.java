package com.example.eventdata_service.service;
import com.example.eventdata_service.models.EventDataRecord;
import com.example.eventdata_service.adapters.RepoAdapter;
import com.example.eventdata_service.utils.DistanceAccumulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class EventDataPartialTransactionService {

    private final List<RepoAdapter> adapters;

    public EventDataPartialTransactionService(List<RepoAdapter> adapters) {
        this.adapters = adapters;
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

