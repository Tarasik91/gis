package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.service.EventDataRecord;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface  EventDataPostgresRepository extends JpaRepository<EventDataPostgres, String> {

    @QueryHints(@QueryHint(name="org.hibernate.fetchSize", value="200000"))
    @Query("select new com.example.spring_boot.service.EventDataRecord(e.latitude, e.longitude, e.timestamp) " +
            "from EventDataPostgres e " +
            "where deviceId=:deviceId and timestamp>= :start and timestamp <= :end")
    Stream<EventDataRecord> findByDeviceIdAndTimestampBetween(@Param("deviceId") long deviceId, @Param("start")  long timestampAfter, @Param("end")  long timestampBefore);
    List<EventDataPostgres> findByDeviceId(long deviceId);

}
