package com.example.spring_boot.adapters;

import com.example.spring_boot.models.EventDataRecord;
import com.example.spring_boot.repository.PostgresEventDataRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class PostgresRepoAdapter implements RepoAdapter {

    private final PostgresEventDataRepository postgresEventDataRepository;

    public PostgresRepoAdapter(PostgresEventDataRepository postgresEventDataRepository) {
        this.postgresEventDataRepository = postgresEventDataRepository;
    }

    @Override
    public String getDbName() {
        return "postgres";
    }

    @Override
    public Stream<EventDataRecord> processPartition(long id, long start, long end) {
        return postgresEventDataRepository.findByDeviceIdAndTimestampBetween(id, start, end);
    }
}
