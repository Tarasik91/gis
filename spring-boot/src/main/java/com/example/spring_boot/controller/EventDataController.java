package com.example.spring_boot.controller;

import com.example.spring_boot.service.EventDataMongoService;
import com.example.spring_boot.service.EventDataPostgresService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EventDataController {
    public EventDataPostgresService postgresService;
    public EventDataMongoService mongoService;


    public EventDataController(EventDataPostgresService postgresService, EventDataMongoService mongoService) {
        this.postgresService = postgresService;
        this.mongoService = mongoService;
    }

    @GetMapping("/device/{deviceId}/track/postgres")
    public ResponseEntity<List<Object>> getDistancesFromPostgres(
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "dailyMode", defaultValue = "false") boolean dailyMode
            ) {
        System.out.println("getDistances Postgres !!!!!!!!!!!");
        List<Object> distances = postgresService.searchDistance(
                deviceId,
                startTime,
                endTime,
                dailyMode
        );
        return ResponseEntity.ok(distances);
    }

    @GetMapping("/device/{deviceId}/track/mongo")
    public ResponseEntity<List<Object>> getDistancesFromMongo(
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "dailyMode", defaultValue = "false") boolean dailyMode,
            Pageable pageable
    ) {
        System.out.println("getDistances Mongo !!!!!!!!!!!");
        List<Object> distances = mongoService.searchDistance(
                deviceId,
                startTime,
                endTime,
                dailyMode,
                pageable
        );
        return ResponseEntity.ok(distances);
    }
}
