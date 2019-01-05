package com.example.logdatapersistservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Model used for local cache key.
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
public class EventKey extends AbstractEvent {
    public EventKey(String id, String type, String host) {
        super(id, type, host);
    }
}
