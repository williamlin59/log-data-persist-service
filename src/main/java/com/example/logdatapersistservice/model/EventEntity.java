package com.example.logdatapersistservice.model;

import lombok.*;

/**
 * Entity class for persist to database
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class EventEntity extends AbstractEvent {
    private Long duration;
    private Boolean alert;

    public EventEntity(String id, String type, String host, Long duration, Boolean alert) {
        super(id, type, host);
        this.duration = duration;
        this.alert = alert;
    }
}
