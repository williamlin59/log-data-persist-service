package com.example.logdatapersistservice.repository.impl;

import com.example.logdatapersistservice.model.EventEntity;
import com.example.logdatapersistservice.util.TestDataHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class EventEntityRepositoryImplTest {
    private static final String SELECT_QUERY = "SELECT * FROM EVENT";
    @Inject
    private EventEntityRepositoryImpl eventEntityRepository;

    @Inject
    private NamedParameterJdbcTemplate jdbcTemplate;


    @Test
    void persistEventEntities() {
        eventEntityRepository.persistEventEntities(TestDataHelper.createEventEntities());
        List<EventEntity> eventEntityList = jdbcTemplate.query(SELECT_QUERY, new BeanPropertyRowMapper(EventEntity.class));
        assertThat(eventEntityList.size()).isEqualTo(2);
        assertThat(eventEntityList.get(0)).isEqualTo(new EventEntity("1", "type1", "host1", 1L, true));
        assertThat(eventEntityList.get(1)).isEqualTo(new EventEntity("2", "type2", "host2", 2L, false));

    }
}