package com.example.spring_boot.repository;

import com.example.spring_boot.entity.EventDataPostgres;
import com.example.spring_boot.models.CoordinateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.stream.Stream;

@Repository
public interface  EventDataPostgresRepository extends JpaRepository<EventDataPostgres, String> {

    @QueryHints(@jakarta.persistence.QueryHint(name="org.hibernate.fetchSize", value="1000"))
    @Query("select new com.example.spring_boot.models.CoordinateDTO(e.latitude,e.longitude,e.timestamp) from EventDataPostgres e where e.deviceId = ?1 and e.timestamp between ?2 and ?3")
    Stream<CoordinateDTO> findByDeviceIdAndTimestampBetween(
            long deviceId,
            long timestampAfter,
            long timestampBefore);

}
