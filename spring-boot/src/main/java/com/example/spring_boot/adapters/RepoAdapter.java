package com.example.spring_boot.adapters;

import com.example.spring_boot.models.EventDataRecord;

import java.util.stream.Stream;

public interface RepoAdapter {

    public Stream<EventDataRecord> processPartition(long id,long start,long end);

}
