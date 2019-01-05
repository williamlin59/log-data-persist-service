package com.example.logdatapersistservice.repository.impl;

import com.example.logdatapersistservice.model.EventEntity;
import com.example.logdatapersistservice.repository.EventEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Repository for persist records to database, using spring transaction manager to do transaction management.
 */
@Repository
@Transactional(propagation = Propagation.SUPPORTS)
@Slf4j
public class EventEntityRepositoryImpl implements EventEntityRepository {
    private static final String INSERT_QUERY = "INSERT INTO EVENT(id, type,host,duration,alert) VALUES (:id, :type, :host, :duration,:alert)";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Inject
    EventEntityRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void persistEventEntities(List<EventEntity> eventEntities) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(eventEntities.toArray());
        jdbcTemplate.batchUpdate(
                INSERT_QUERY, batch);
        log.debug("{} eventEntities persisted to database", eventEntities.size());
    }
}
