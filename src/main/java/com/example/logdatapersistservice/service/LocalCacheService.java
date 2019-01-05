package com.example.logdatapersistservice.service;


import com.example.logdatapersistservice.model.EventKey;
import com.example.logdatapersistservice.model.State;

import java.util.List;
import java.util.Map;

public interface LocalCacheService {
    void add(List<String> eventJsonStrings);

    Map<EventKey, Map<State, Long>> getMap();
}
