package com.example.eventdata_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class TackPayload {
    long startTime;
    long endTime;
    boolean isDailyMode;
}
