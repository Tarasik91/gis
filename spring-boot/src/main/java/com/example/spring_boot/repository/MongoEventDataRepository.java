package com.example.spring_boot.repository;

import com.example.spring_boot.entity.MongoEventData;
import com.example.spring_boot.models.EventDataRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface MongoEventDataRepository extends MongoRepository<MongoEventData, Long> {

    //@Query(value = "{ 'deviceId' : ?0, 'start' : ?1, 'end' : ?2 }", fields = "{ 'latitude' : 1, 'longitude' : 1, 'timestamp' : 1}")
    Stream<EventDataRecord> findByDeviceIdAndTimestampBetween(
            long deviceId,
            long start,
            long end
    );

    List<MongoEventData> findByDeviceIdAndTimestampBetween(
            long deviceId,
            long start,
            long end,
            Pageable pageable
    );
}
