package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventDataMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface   EventDataMongoRepository extends MongoRepository<EventDataMongo, Long> {
    List<EventDataMongo> findByDeviceIdAndTimestampBetween(
            long deviceId,
            long timestampAfter,
            long timestampBefore
    );
}
