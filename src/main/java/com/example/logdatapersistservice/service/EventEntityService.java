package com.example.logdatapersistservice.service;

import com.example.logdatapersistservice.model.EventKey;
import com.example.logdatapersistservice.model.State;

import java.util.Map;

public interface EventEntityService {
    void persistEventEntity(Map<EventKey, Map<State, Long>> localCacheMap);
}
