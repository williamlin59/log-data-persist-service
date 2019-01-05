package com.example.logdatapersistservice.component;

import com.example.logdatapersistservice.service.EventEntityService;
import com.example.logdatapersistservice.service.LocalCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class LogFileLauncherTest {
    @Spy
    private LocalCacheService localCacheServiceSpy;

    @Mock
    private EventEntityService eventEntityServiceMock;

    private LogFileLauncher logFileLauncher;

    @BeforeEach
    void setUp() {
        initMocks(this);
        logFileLauncher = new LogFileLauncher(localCacheServiceSpy, eventEntityServiceMock, 2);
    }

    @Test
    void testLaunch() throws URISyntaxException, IOException {
        doNothing().when(eventEntityServiceMock).persistEventEntity(anyMap());
        Path path = Paths.get(getClass().getClassLoader()
                .getResource("test-data.json").toURI());
        logFileLauncher.launch(path.toString());
        verify(localCacheServiceSpy, times(3)).add(anyList());
        verify(eventEntityServiceMock).persistEventEntity(anyMap());
    }
}