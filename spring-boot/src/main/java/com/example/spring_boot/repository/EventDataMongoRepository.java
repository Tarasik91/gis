package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventDataMongo;
import com.example.spring_boot.models.CoordinateDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface   EventDataMongoRepository extends MongoRepository<EventDataMongo, Long> {


    Stream<CoordinateDTO> findByDeviceIdAndTimestampBetween(
            long deviceId,
            long timestampAfter,
            long timestampBefore
    );
}
