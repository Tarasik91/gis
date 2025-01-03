package com.example.spring_boot.controller;

import com.example.spring_boot.adapters.MongoRepoAdapter;
import com.example.spring_boot.adapters.PostgresRepoAdapter;
import com.example.spring_boot.service.EventDataPartialTransactionService;
import com.example.spring_boot.service.MongoEventDataService;
import com.example.spring_boot.service.PostgresEventDataService;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public class EventDataControllerV2 {

    private final PostgresEventDataService postgresEventDataService;
    private final MongoEventDataService mongoEventDataService;
    private final PostgresRepoAdapter postgresRepoAdapter;
    private final MongoRepoAdapter mongoRepoAdapter;

    public EventDataControllerV2(
            PostgresEventDataService postgresEventDataService,
            MongoEventDataService mongoEventDataService,
            PostgresRepoAdapter postgresRepoAdapter,
            MongoRepoAdapter mongoRepoAdapter) {
        this.postgresEventDataService = postgresEventDataService;
        this.mongoEventDataService = mongoEventDataService;
        this.postgresRepoAdapter = postgresRepoAdapter;
        this.mongoRepoAdapter = mongoRepoAdapter;
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
        return ResponseEntity.ok(requestOctopusSearch(db,deviceId,startTime,endTime,page,size,dailyMode));
    }


    private Object requestOctopusSearch(String db, long id, long startTime, long endTime,int page,int size, boolean dailyMode) {
        switch (db) {
            case "postgres":
                return postgresEventDataService.octopusDistanceSearch(postgresRepoAdapter,id,startTime,endTime,dailyMode,page,size);
            case "mongo":
                return mongoEventDataService.octopusDistanceSearch(mongoRepoAdapter,id,startTime,endTime,dailyMode,page,size);
            default:
                return null;
        }
    }
}
