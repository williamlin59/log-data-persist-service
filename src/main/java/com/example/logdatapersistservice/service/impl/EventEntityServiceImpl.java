package com.example.logdatapersistservice.service.impl;

import com.example.logdatapersistservice.exception.LogDataPersistServiceException;
import com.example.logdatapersistservice.model.EventEntity;
import com.example.logdatapersistservice.model.EventKey;
import com.example.logdatapersistservice.model.State;
import com.example.logdatapersistservice.repository.EventEntityRepository;
import com.example.logdatapersistservice.service.EventEntityService;
import com.landawn.streamex.StreamEx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

/**
 * Perform business logic to compute duration and set alert. Using streamEx to stream data and async persist to database.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Slf4j
public class EventEntityServiceImpl implements EventEntityService {
    private static final int DEFAULT_TIME = 4;
    private final EventEntityRepository eventEntityRepository;
    private final Integer persistChunkSize;

    @Inject
    EventEntityServiceImpl(@Value("${persist-chunk-size}") Integer persistChunkSize,
                           EventEntityRepository eventEntityRepository) {
        this.persistChunkSize = persistChunkSize;
        this.eventEntityRepository = eventEntityRepository;
    }

    @Override
    public void persistEventEntity(Map<EventKey, Map<State, Long>> localCacheMap) {
        try (StreamEx<Map.Entry<EventKey, Map<State, Long>>> streamEx = StreamEx.of(localCacheMap.entrySet())) {
            streamEx.map(this::mapEntryToEventEntity).splitToList(persistChunkSize).parallel().forEach(eventEntityRepository::persistEventEntities);
        }
        log.info("{} records persisted to database", localCacheMap.size());
    }

    private EventEntity mapEntryToEventEntity(Map.Entry<EventKey, Map<State, Long>> eventKeyMapEntry) {
        EventKey key = eventKeyMapEntry.getKey();
        Map<State, Long> map = eventKeyMapEntry.getValue();
        long duration = Optional.ofNullable(map.get(State.FINISHED)).orElseThrow(() -> new LogDataPersistServiceException("No finished records for id:" + key.getId()))
                - Optional.ofNullable(map.get(State.STARTED)).orElseThrow(() -> new LogDataPersistServiceException("No started records for id:" + key.getId()));
        return new EventEntity(key.getId(), key.getType(), key.getHost(), duration, duration > DEFAULT_TIME);
    }

}
