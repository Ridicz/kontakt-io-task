package io.kontak.apps.storage;

import io.kontak.apps.dto.Thermometer;
import io.kontak.apps.event.Anomaly;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.containsInAnyOrder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnomalyStorageServiceTest extends AbstractIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AnomalyRepository anomalyRepository;

    private AnomalyStorageService anomalyStorageService;

    @BeforeAll
    void setUp() {
        anomalyStorageService = new AnomalyStorageService(mongoTemplate, anomalyRepository);
    }

    @AfterEach
    void cleanUp() {
        this.anomalyRepository.deleteAll();
    }

    @Test
    void testAnomaliesPerRoom() {
        //given
        var anomalies = createAnomalies();
        var page = PageRequest.of(0, 50);

        //when
        anomalies.forEach(anomalyStorageService::save);
        var anomaliesFirstRoom = anomalyStorageService.getAnomaliesPerRoom("1", page);
        var anomaliesSecondRoom = anomalyStorageService.getAnomaliesPerRoom("2", page);
        var anomaliesThirdRoom = anomalyStorageService.getAnomaliesPerRoom("3", page);
        var anomaliesFourthRoom = anomalyStorageService.getAnomaliesPerRoom("4", page);

        //then
        assertEquals(4, anomaliesFirstRoom.getTotalElements());
        assertEquals(2, anomaliesSecondRoom.getTotalElements());
        assertEquals(1, anomaliesThirdRoom.getTotalElements());
        assertEquals(4, anomaliesFourthRoom.getTotalElements());
    }


    @Test
    void testAnomaliesPerThermometer() {
        //given
        var anomalies = createAnomalies();
        var page = PageRequest.of(0, 50);

        //when
        anomalies.forEach(anomalyStorageService::save);
        var anomaliesFirstThermometer = anomalyStorageService.getAnomaliesPerThermometer("1", page);
        var anomaliesSecondThermometer = anomalyStorageService.getAnomaliesPerThermometer("2", page);
        var anomaliesThirdThermometer = anomalyStorageService.getAnomaliesPerThermometer("3", page);
        var anomaliesFourthThermometer = anomalyStorageService.getAnomaliesPerThermometer("4", page);
        var anomaliesFifthThermometer = anomalyStorageService.getAnomaliesPerThermometer("5", page);

        //then
        assertEquals(3, anomaliesFirstThermometer.getTotalElements());
        assertEquals(1, anomaliesSecondThermometer.getTotalElements());
        assertEquals(2, anomaliesThirdThermometer.getTotalElements());
        assertEquals(1, anomaliesFourthThermometer.getTotalElements());
        assertEquals(4, anomaliesFifthThermometer.getTotalElements());
    }

    @Test
    void testThermometersWithAnomaliesThreshold() {
        //given
        var anomalies = createAnomalies();

        //when
        anomalies.forEach(anomalyStorageService::save);
        List<Thermometer> thermometers = anomalyStorageService.getThermometersWithAnomaliesThreshold(Optional.of(2L));

        //then
        assertNotNull(thermometers);
        assertEquals(2, thermometers.size());
        assertThat(thermometers.stream().map(Thermometer::getThermometerId).collect(Collectors.toList()), containsInAnyOrder("1", "5"));
    }

    private List<Anomaly> createAnomalies() {
        return List.of(
                new Anomaly(21.30, "1", "1", Instant.now()),
                new Anomaly(21.31, "1", "2", Instant.now()),
                new Anomaly(21.32, "1", "1", Instant.now()),
                new Anomaly(21.32, "1", "1", Instant.now()),
                new Anomaly(21.33, "2", "3", Instant.now()),
                new Anomaly(21.34, "2", "3", Instant.now()),
                new Anomaly(21.35, "3", "4", Instant.now()),
                new Anomaly(22.35, "4", "5", Instant.now()),
                new Anomaly(23.35, "4", "5", Instant.now()),
                new Anomaly(24.35, "4", "5", Instant.now()),
                new Anomaly(25.35, "4", "5", Instant.now())
        );
    }
}
