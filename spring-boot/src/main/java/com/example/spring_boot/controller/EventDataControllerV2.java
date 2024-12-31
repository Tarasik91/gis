package com.example.spring_boot.controller;

import com.example.spring_boot.service.EventDataMongoService;
import com.example.spring_boot.service.EventDataTransactionService;
import com.example.spring_boot.utils.DistanceAccumulator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public class EventDataControllerV2 {
    private final EventDataTransactionService eventDataTransactionService;

    public EventDataControllerV2(EventDataTransactionService eventDataTransactionService) {
        this.eventDataTransactionService = eventDataTransactionService;
    }

    @GetMapping("/device/{deviceId}/track/mongo")
    public ResponseEntity<Object> getDistancesFromMongo(
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "isDaily", defaultValue = "false") boolean dailyMode
    ) {

        var acumulator = new DistanceAccumulator();
        eventDataTransactionService.processMongoRange(deviceId, startTime, endTime, acumulator);
        return ResponseEntity.ok(acumulator.getTotalDistance());
    }

    @GetMapping("/device/{deviceId}/track/postgres")
    public ResponseEntity<Object> getDistancesFromPostgres(
            @PathVariable Long deviceId,
            @RequestParam Long startTime,
            @RequestParam Long endTime,
            @RequestParam(value = "isDaily", defaultValue = "false") boolean dailyMode
    ) {

        var acumulator = new DistanceAccumulator();
        eventDataTransactionService.processRange(deviceId, startTime, endTime, acumulator);
        return ResponseEntity.ok(acumulator.getTotalDistance());
    }
}
