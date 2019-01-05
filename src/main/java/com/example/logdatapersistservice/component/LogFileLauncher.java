package com.example.logdatapersistservice.component;

import com.example.logdatapersistservice.service.EventEntityService;
import com.example.logdatapersistservice.service.LocalCacheService;
import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Launcher working as main to read records from file, produce microbatch and persist to local cache,
 * then stream from local cache persist to database.
 */
@Component
@Slf4j
public class LogFileLauncher {
    private final LocalCacheService localCacheService;
    private final Integer fileChunkSize;
    private final EventEntityService eventEntityService;

    @Inject
    LogFileLauncher(LocalCacheService localCacheService,
                    EventEntityService eventEntityService,
                    @Value("${file-chunk-size}") Integer fileChunkSize) {
        this.localCacheService = localCacheService;
        this.fileChunkSize = fileChunkSize;
        this.eventEntityService = eventEntityService;
    }

    void launch(String filePath) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
            Iterators.partition(stream.iterator(), fileChunkSize).forEachRemaining(chunk -> {
                CompletableFuture<Void> result = CompletableFuture.runAsync(() -> localCacheService.add(chunk));
                completableFutureList.add(result);
            });
            CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0])).join();
            log.info("Completed reading data to local cache with {} tasks completed", completableFutureList.size());
        }
        eventEntityService.persistEventEntity(localCacheService.getMap());

    }

}
