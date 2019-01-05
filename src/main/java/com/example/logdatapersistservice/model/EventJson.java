package com.example.logdatapersistservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Model object containing data from input file
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
public class EventJson extends AbstractEvent {
    private final State state;
    private final Long timestamp;

    public EventJson(String id, String type, String host, State state, Long timestamp) {
        super(id, type, host);
        this.state = state;
        this.timestamp = timestamp;
    }
}
