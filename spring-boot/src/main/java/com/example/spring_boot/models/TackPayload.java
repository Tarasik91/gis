package com.example.spring_boot.models;

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
