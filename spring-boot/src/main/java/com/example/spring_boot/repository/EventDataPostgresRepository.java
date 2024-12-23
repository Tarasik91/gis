package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventData;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface  EventDataPostgresRepository extends CrudRepository<EventData, String> {

    List<EventData> findByDeviceIdAndTimestampBetween(long deviceId, long timestampAfter, long timestampBefore);

}
