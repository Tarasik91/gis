package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDataRepository extends CrudRepository<EventData, Long> {
}
