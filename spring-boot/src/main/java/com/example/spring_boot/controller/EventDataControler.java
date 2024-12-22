package com.example.spring_boot.controller;

import com.example.spring_boot.entity.EventData;
import com.example.spring_boot.repository.EventDataPostgresRepository;
import com.example.spring_boot.service.EventDataService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EventDataControler {
    public EventDataService service;


    public EventDataControler(EventDataService service) {
        this.service = service;
    }

    @GetMapping("/postgres-events")
    public ResponseEntity<Page<EventData>> getProducts(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<EventData> products = service.search(search, page, size);
        return ResponseEntity.ok(products);
    }

}
