package com.example.eventdata_service.adapters;

import com.example.eventdata_service.models.EventDataRecord;
import com.example.eventdata_service.repository.PostgresEventDataRepository;
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
