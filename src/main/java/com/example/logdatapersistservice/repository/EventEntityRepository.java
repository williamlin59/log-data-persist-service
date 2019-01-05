package com.example.logdatapersistservice.repository;

import com.example.logdatapersistservice.model.EventEntity;

import java.util.List;

public interface EventEntityRepository {
    void persistEventEntities(List<EventEntity> eventEntities);
}
