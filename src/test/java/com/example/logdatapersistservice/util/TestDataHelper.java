package com.example.logdatapersistservice.util;

import com.example.logdatapersistservice.model.EventEntity;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class TestDataHelper {
    private TestDataHelper() {
    }

    public static List<String> initialTestData() {
        Path path;
        try {
            path = Paths.get(TestDataHelper.class.getClassLoader()
                    .getResource("test-data.json").toURI());
            return Files.readAllLines(path);
        } catch (Exception e) {
            log.error("Unable to read test data ", e);
        }
        return null;
    }

    public static List<EventEntity> createEventEntities() {
        return ImmutableList.of(new EventEntity("1", "type1", "host1", 1L, true),
                new EventEntity("2", "type2", "host2", 2L, false));
    }
}
