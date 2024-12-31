package com.example.spring_boot.controller;

import com.example.spring_boot.service.EventDataMongoService;
import com.example.spring_boot.service.EventDataPostgresService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EventDataController {
    private final EventDataPostgresService postgresService;
    private final EventDataMongoService mongoService;

    public EventDataController(EventDataPostgresService postgresService, EventDataMongoService mongoService) {
        this.postgresService = postgresService;
        this.mongoService = mongoService;
    }

    @GetMapping("/device/{deviceId}/track/postgres")
    public ResponseEntity<Object> getDistancesFromPostgres(
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "isDaily", defaultValue = "false") boolean dailyMode
    ) {
        Object distances = postgresService.searchDistance(
                deviceId,
                startTime,
                endTime,
                dailyMode
        );
        return ResponseEntity.ok(distances);
    }

    @GetMapping("/device/{deviceId}/track/mongo")
    public ResponseEntity<Object> getDistancesFromMongo(
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "isDaily", defaultValue = "false") boolean dailyMode
    ) {
        Object distances = mongoService.searchDistance(
                deviceId,
                startTime,
                endTime,
                dailyMode
        );
        return ResponseEntity.ok(distances);
    }


    @GetMapping("/device/{deviceId}/events/mongo")
    public ResponseEntity<Object> getEvents(
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Object distances = mongoService.getEvents(
                deviceId,
                startTime,
                endTime,
                PageRequest.of(page, size)
        );
        return ResponseEntity.ok(distances);
    }
}
