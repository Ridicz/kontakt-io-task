package io.kontak.apps.storage;

import io.kontak.apps.storage.entity.AnomalyEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnomalyRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private AnomalyRepository anomalyRepository;

    @AfterEach
    void cleanUp() {
        this.anomalyRepository.deleteAll();
    }

    @Test
    void testRepositorySaveAndFindAll() {
        //given
        var anomaly = AnomalyEntity.builder()
                .temperature(21.35)
                .thermometerId("1")
                .roomId("1")
                .timestamp(Instant.now())
                .build();

        this.anomalyRepository.save(anomaly);

        //when
        List<AnomalyEntity> customers = anomalyRepository.findAll();

        //then
        assertEquals(1, customers.size());
    }
}
