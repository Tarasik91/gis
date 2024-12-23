package com.example.spring_boot.controller;

import com.example.spring_boot.entity.EventData;
import com.example.spring_boot.models.TackPayload;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import com.example.spring_boot.service.EventDataService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EventDataControler {
    public EventDataService service;


    public EventDataControler(EventDataService service) {
        this.service = service;
    }

    @GetMapping("/device/{deviceId}/track")
    public ResponseEntity<List<Object>> getDistances(@PathVariable Long deviceId,@RequestBody TackPayload payload) {
        System.out.println("getDistances!!!!!!!!!!!");
        List<Object> distances = service.searchDistance(
                deviceId,
                payload.getStartTime(),
                payload.getEndTime(),
                payload.isDailyMode()
                );
        return ResponseEntity.ok(distances);
    }
}
