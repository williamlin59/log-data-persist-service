package com.example.logdatapersistservice.component;

import com.example.logdatapersistservice.exception.LogDataPersistServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

/**
 * Taking file path from commandline for reading. Using CommandLineRunner.
 */
@Component
@Slf4j
@Profile("!test")
public class LogFileRunner implements CommandLineRunner {
    private final LogFileLauncher logFileLauncher;
    private boolean flag = true;

    LogFileRunner(LogFileLauncher logFileLauncher) {
        this.logFileLauncher = logFileLauncher;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (flag) {
            log.info("Please input the full path of a give file (input q to terminate):");
            String filePath = scanner.nextLine();
            if ("q".equalsIgnoreCase(filePath)) {
                return;
            }
            try {
                logFileLauncher.launch(filePath);
            } catch (IOException e) {
                log.error("Unable to read file : {}", filePath, e);
            } catch (Exception e) {
                throw new LogDataPersistServiceException("Unable to persist log data to database", e);
            }
        }
    }

}
