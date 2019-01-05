package com.example.logdatapersistservice.model;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@Setter
abstract class AbstractEvent {
    private String id;
    private String type;
    private String host;
}
