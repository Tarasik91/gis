package com.example.eventdata_service.repository;

import com.example.eventdata_service.models.EventDataRecord;
import com.example.eventdata_service.entity.PostgresEventData;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface PostgresEventDataRepository extends JpaRepository<PostgresEventData, String> {

    @QueryHints(
            {
                    @QueryHint(name = "org.hibernate.fetchSize", value = "200000"),
                    @QueryHint(name = "org.hibernate.readOnly", value = "true"),
                    @QueryHint(name = "org.hibernate.cacheMode", value = "IGNORE"),
                    @QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")
            }
    )
    @Query("select new com.example.spring_boot.models.EventDataRecord(e.latitude, e.longitude, e.timestamp) " +
            "from PostgresEventData e " +
            "where e.deviceId=:deviceId and timestamp>= :start and timestamp <= :end")
    Stream<EventDataRecord> findByDeviceIdAndTimestampBetween(@Param("deviceId") long deviceId, @Param("start") long start, @Param("end") long end);


    List<PostgresEventData> findByDeviceIdAndTimestampBetween(@Param("deviceId") long deviceId, @Param("start") long start, @Param("end") long end, Pageable pageable);

    List<PostgresEventData> findByDeviceId(long deviceId);

}
