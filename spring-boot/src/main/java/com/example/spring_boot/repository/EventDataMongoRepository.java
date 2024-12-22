package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventData;
import com.example.spring_boot.entity.EventDataMongo;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface   EventDataMongoRepository extends MongoRepository<EventDataMongo, Long> {
//    Page<EventData> findBy(String key, Pageable pageable);
}
