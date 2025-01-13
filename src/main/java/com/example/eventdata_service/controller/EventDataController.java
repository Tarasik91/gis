package com.example.eventdata_service.controller;

import com.example.eventdata_service.dto.EventDataPayload;
import com.example.eventdata_service.dto.EventDataResponse;
import com.example.eventdata_service.service.EventDataService;
import com.example.eventdata_service.service.MongoEventDataService;
import com.example.eventdata_service.service.PostgresEventDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EventDataController {

    private final List<EventDataService> serviceList;


    public EventDataController(List<EventDataService> serviceList) {
        this.serviceList = serviceList;
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

        var payload = new EventDataPayload(db, deviceId, startTime, endTime, page, size, dailyMode);
        return ResponseEntity.ok(getEvents(payload));
    }

    private List<EventDataResponse> getEvents(EventDataPayload payload) {
        final List<EventDataResponse> result = new ArrayList<>();
        serviceList
                .stream()
                .filter(it -> it.getDbName().equals(payload.db()))
                .findFirst()
                .ifPresent(it ->
                        result.addAll(it.getEvents(payload))
                );
        return result;
    }
}
