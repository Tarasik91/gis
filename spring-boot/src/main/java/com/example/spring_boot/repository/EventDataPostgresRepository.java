package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventData;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface  EventDataPostgresRepository extends CrudRepository<EventData, String> {

    Page<EventData> findByLatitudeContaining(String key, Pageable pageable);
}
