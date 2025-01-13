package com.example.eventdata_service.controller;

import com.example.eventdata_service.dto.EventDataPayload;
import com.example.eventdata_service.service.EventDataService;
import com.example.eventdata_service.service.MongoEventDataService;
import com.example.eventdata_service.service.PostgresEventDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class EventDataControllerV2 {

    List<EventDataService> eventDataServices;

    public EventDataControllerV2(
            PostgresEventDataService postgresEventDataService,
            MongoEventDataService mongoEventDataService) {
        eventDataServices = new ArrayList<>();
        eventDataServices.add(postgresEventDataService);
        eventDataServices.add(mongoEventDataService);
    }


    @GetMapping("/device/{deviceId}/distance/{db}")
    public ResponseEntity<Object> getDistancesRequest(
            @PathVariable String db,
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "isDaily", defaultValue = "false") boolean dailyMode
    ) {
        var serviceOptional = eventDataServices.stream()
                .filter(it -> it.getDbName().equals(db))
                .findFirst();

        if (serviceOptional.isPresent()) {
            var service = serviceOptional.get();
            var payload = new EventDataPayload(db, deviceId, startTime, endTime, page, size, dailyMode);
            var result = service.octopusDistanceSearch(payload);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body("Service not found for db: " + db);
        }
    }
}
