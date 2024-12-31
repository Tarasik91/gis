package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.models.EventDataRecord;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface EventDataMongoRepository extends MongoRepository<EventDataMongo, Long> {

    //@Query(value = "{ 'deviceId' : ?0, 'start' : ?1, 'end' : ?2 }", fields = "{ 'latitude' : 1, 'longitude' : 1, 'timestamp' : 1}")
    Stream<EventDataRecord> findByDeviceIdAndTimestampBetween(
            long deviceId,
            long start,
            long end
    );

    List<EventDataMongo> findByDeviceIdAndTimestampBetween(
            long deviceId,
            long start,
            long end,
            Pageable pageable
    );
}
