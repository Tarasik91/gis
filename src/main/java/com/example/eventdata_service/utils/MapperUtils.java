package com.example.eventdata_service.utils;

import com.example.eventdata_service.dto.EventDataResponse;
import com.example.eventdata_service.entity.IEventData;

import java.text.SimpleDateFormat;
public class MapperUtils {
    public static EventDataResponse map2Response(IEventData entity) {
        EventDataResponse response = new EventDataResponse();
        response.setLatitude(entity.getLatitude());
        response.setLongitude(entity.getLongitude());
        response.setSensorData(entity.getSensorData());
        response.setSateliteCount(entity.getSatelliteCount());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        response.setDate(dateFormat.format(entity.getTimestamp()));
        return response;
    }
}
