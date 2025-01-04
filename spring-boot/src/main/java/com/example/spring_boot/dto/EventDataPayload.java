package com.example.spring_boot.dto;

public record EventDataPayload(String db,long deviceId,long startTime,long endTime,int page,int size,boolean isDaily) {
}
