package com.example.logdatapersistservice.service.impl;

import com.example.logdatapersistservice.exception.LogDataPersistServiceException;
import com.example.logdatapersistservice.model.EventEntity;
import com.example.logdatapersistservice.model.EventKey;
import com.example.logdatapersistservice.model.State;
import com.example.logdatapersistservice.repository.EventEntityRepository;
import com.example.logdatapersistservice.service.EventEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

class EventEntityServiceImplTest {
    private static final int PERSIST_CHUNK_SIZE = 1;
    @Mock
    private EventEntityRepository eventEntityRepositoryMock;
    private EventEntityService eventEntityService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        eventEntityService = new EventEntityServiceImpl(PERSIST_CHUNK_SIZE, eventEntityRepositoryMock);
    }

    @Test
    void testPersistEventEntity() {
        Map<EventKey, Map<State, Long>> eventKeyMap = new ConcurrentHashMap<>();
        eventKeyMap.put(getEventKey("1"), getStateLongMap());
        eventKeyMap.put(getEventKey("2"), getStateLongMap());
        eventKeyMap.put(getEventKey("3"), getStateLongMap());
        doNothing().when(eventEntityRepositoryMock).persistEventEntities(anyList());
        eventEntityService.persistEventEntity(eventKeyMap);
        verify(eventEntityRepositoryMock).persistEventEntities(getEventEntityList("1"));
        verify(eventEntityRepositoryMock).persistEventEntities(getEventEntityList("2"));
        verify(eventEntityRepositoryMock).persistEventEntities(getEventEntityList("3"));
    }

    @Test
    void testFailedPersistEventEntityNoFinishedRecord() {
        Map<EventKey, Map<State, Long>> eventKeyMap = new ConcurrentHashMap<>();
        eventKeyMap.put(getEventKey("1"), getStateLongMapWithOneRecord(State.FINISHED));
        doNothing().when(eventEntityRepositoryMock).persistEventEntities(anyList());
        assertThrows(LogDataPersistServiceException.class, () -> {
            eventEntityService.persistEventEntity(eventKeyMap);
        });
    }

    @Test
    void testFailedPersistEventEntityNoStartedRecord() {
        Map<EventKey, Map<State, Long>> eventKeyMap = new ConcurrentHashMap<>();
        eventKeyMap.put(getEventKey("1"), getStateLongMapWithOneRecord(State.STARTED));
        doNothing().when(eventEntityRepositoryMock).persistEventEntities(anyList());
        assertThrows(LogDataPersistServiceException.class, () -> {
            eventEntityService.persistEventEntity(eventKeyMap);
        });
    }

    private List<EventEntity> getEventEntityList(String id) {
        List<EventEntity> eventEntities = new ArrayList<>();
        eventEntities.add(createNewEventEntity(id));
        return eventEntities;
    }

    private EventEntity createNewEventEntity(String id) {
        return new EventEntity(id, "Test", "localhost", 1234L - 123L, true);
    }

    private EventKey getEventKey(String id) {
        return new EventKey(id, "Test", "localhost");
    }

    private Map<State, Long> getStateLongMap() {
        Map<State, Long> stateLongMap = new ConcurrentHashMap<>();
        stateLongMap.put(State.FINISHED, 1234L);
        stateLongMap.put(State.STARTED, 123L);
        return stateLongMap;
    }

    private Map<State, Long> getStateLongMapWithOneRecord(State state) {
        Map<State, Long> stateLongMap = getStateLongMap();
        stateLongMap.remove(state);
        return stateLongMap;
    }

}