package com.example.eventdata_service.adapters;

import com.example.eventdata_service.models.EventDataRecord;

import java.util.stream.Stream;

public interface RepoAdapter {

    public String getDbName();

    public Stream<EventDataRecord> processPartition(long id, long start, long end);

}
