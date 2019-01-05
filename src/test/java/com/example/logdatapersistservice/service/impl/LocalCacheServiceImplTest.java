package com.example.logdatapersistservice.service.impl;

import com.example.logdatapersistservice.model.EventKey;
import com.example.logdatapersistservice.model.State;
import com.example.logdatapersistservice.service.LocalCacheService;
import com.example.logdatapersistservice.util.TestDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LocalCacheServiceImplTest {
    private static final List<String> TEST_DATA = TestDataHelper.initialTestData();
    private LocalCacheService localCacheService;

    @BeforeEach
    void setUp() {
        localCacheService = new LocalCacheServiceImpl();
    }

    @Test
    void testAdd() {
        localCacheService.add(TEST_DATA);
        Map<EventKey, Map<State, Long>> map = localCacheService.getMap();
        assertThat(map.size()).isEqualTo(3);
        verifyStateMap(map.get(new EventKey("scsmbstgra", "APPLICATION_LOG", "12345")), 1491377495212L, 1491377495217L);
        verifyStateMap(map.get(new EventKey("scsmbstgrb", null, null)), 1491377495213L, 1491377495216L);
        verifyStateMap(map.get(new EventKey("scsmbstgrc", null, null)), 1491377495210L, 1491377495218L);
    }

    @Test
    void testGetMap() {
        assertThat(localCacheService.getMap()).isNotNull();
    }

    private void verifyStateMap(Map<State, Long> stateLongMap, long startedTimestamp, long finishedTimestamp) {
        assertThat(stateLongMap).isNotNull();
        assertThat(stateLongMap.get(State.FINISHED)).isEqualTo(finishedTimestamp);
        assertThat(stateLongMap.get(State.STARTED)).isEqualTo(startedTimestamp);
    }
}