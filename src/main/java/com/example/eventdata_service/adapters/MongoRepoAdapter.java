package com.example.eventdata_service.adapters;

import com.example.eventdata_service.models.EventDataRecord;
import com.example.eventdata_service.repository.MongoEventDataRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;


@Component
public class MongoRepoAdapter implements RepoAdapter {
    private final MongoEventDataRepository mongoEventDataRepository;

    public MongoRepoAdapter(MongoEventDataRepository mongoEventDataRepository) {
        this.mongoEventDataRepository = mongoEventDataRepository;
    }


    @Override
    public String getDbName() {
        return "mongo";
    }

    @Override
    public Stream<EventDataRecord> processPartition(long deviceId, long starTime, long endTime) {
        return mongoEventDataRepository.findByDeviceIdAndTimestampBetween(deviceId, starTime, endTime);
    }
}
