package com.example.spring_boot.controller;

import com.example.spring_boot.dto.EventDataPayload;
import com.example.spring_boot.dto.EventDataResponse;
import com.example.spring_boot.service.EventDataService;
import com.example.spring_boot.service.MongoEventDataService;
import com.example.spring_boot.service.PostgresEventDataService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EventDataController {

    private final List<EventDataService> serviceList;


    public EventDataController(PostgresEventDataService postgresService, MongoEventDataService mongoService) {
        serviceList = new ArrayList<>();
        serviceList.add(postgresService);
        serviceList.add(mongoService);
    }

    @GetMapping("/device/{deviceId}/distance/{db}")
    public ResponseEntity<Object> getEventsRequest(
            @PathVariable String db,
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "isDaily", defaultValue = "false") boolean dailyMode
    ) {

        var payload = new EventDataPayload(db,deviceId,startTime,endTime,page,size,dailyMode);
        return ResponseEntity.ok(getEvents(payload));
    }

    private List<EventDataResponse> getEvents(EventDataPayload payload) {
        final List<EventDataResponse> result = new ArrayList<>();
        serviceList.stream().filter(it->it.getDbName().equals(payload.db()))
                .findFirst().ifPresent(it-> {
            result.addAll(it.getEvents(payload));
        });
        return result;
    }
}
