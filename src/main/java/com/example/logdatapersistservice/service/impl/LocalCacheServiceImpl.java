package com.example.logdatapersistservice.service.impl;

import com.example.logdatapersistservice.model.EventJson;
import com.example.logdatapersistservice.model.EventKey;
import com.example.logdatapersistservice.model.State;
import com.example.logdatapersistservice.service.LocalCacheService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allow adding records or getting cache map instance.
 * Assumption: if duplicate state happened under same key(multiple start finish records under same key), only
 * the first one will be persist to map.
 */
@Service
@Slf4j
public class LocalCacheServiceImpl implements LocalCacheService {
    private final Map<EventKey, Map<State, Long>> localCacheMap;
    private final Gson gson;

    @Inject
    public LocalCacheServiceImpl() {
        localCacheMap = new ConcurrentHashMap<>();
        gson = new Gson();
    }

    @Override
    public void add(List<String> eventJsonStrings) {
        for (String eventJsonString : eventJsonStrings) {
            EventJson eventJson = gson.fromJson(eventJsonString, EventJson.class);
            EventKey eventKey = convertEventJsonToEventKey(eventJson);
            localCacheMap.computeIfAbsent(eventKey, value -> new ConcurrentHashMap<>());
            localCacheMap.get(eventKey).putIfAbsent(eventJson.getState(), eventJson.getTimestamp());
        }
        log.debug("{} records from file saved to local cache", eventJsonStrings.size());
    }

    @Override
    public Map<EventKey, Map<State, Long>> getMap() {
        return localCacheMap;
    }

    private EventKey convertEventJsonToEventKey(EventJson eventJson) {
        return new EventKey(eventJson.getId(), eventJson.getType(), eventJson.getHost());
    }
}
