package com.example.spring_boot.controller;

import com.example.spring_boot.dto.EventDataResponse;
import com.example.spring_boot.service.MongoEventDataService;
import com.example.spring_boot.service.PostgresEventDataService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EventDataController {
    private final PostgresEventDataService postgresService;
    private final MongoEventDataService mongoService;

    public EventDataController(PostgresEventDataService postgresService, MongoEventDataService mongoService) {
        this.postgresService = postgresService;
        this.mongoService = mongoService;
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
        return ResponseEntity.ok(getEvents(db,deviceId,startTime,endTime,page,size));
    }

    private List<EventDataResponse> getEvents(
            String db,
            long deviceId,
            long startTime,
            long endTime,
            int page,
            int size) {
        if (db.equals("postgres")) {
            return postgresService.getEvents(
                    deviceId,
                    startTime,
                    endTime,
                    PageRequest.of(page, size)
            );
        }
        return mongoService.getEvents(
                deviceId,
                startTime,
                endTime,
                PageRequest.of(page, size)
        );
    }
}
