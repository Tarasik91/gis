package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventDataPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface  EventDataPostgresRepository extends JpaRepository<EventDataPostgres, String> {

    List<EventDataPostgres> findByDeviceIdAndTimestampBetween( long deviceId, long timestampAfter, long timestampBefore);
    List<EventDataPostgres> findByDeviceId(long deviceId);

}
