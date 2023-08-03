package io.kontakt.apps.anomaly.detector;

import io.kontak.apps.anomaly.detector.detector.AnomalyDetector;
import io.kontak.apps.anomaly.detector.detector.MeanAnomalyDetector;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.containsInAnyOrder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MeanAnomalyDetectorTest {

    private final AnomalyDetector anomalyDetector = new MeanAnomalyDetector();

    @BeforeAll
    public void setUp() {
        ReflectionTestUtils.setField(anomalyDetector, "anomalyThreshold", 5.0);
        ReflectionTestUtils.setField(anomalyDetector, "sampleSize", 10);
    }

    @Test
    public void testEmptyInput() {
        //given
        //when
        var anomaly = anomalyDetector.apply(List.of());

        //then
        Assertions.assertNotNull(anomaly);
        Assertions.assertTrue(anomaly.isEmpty());
    }

    @Test
    public void testSingleInput() {
        //given
        var reading = new TemperatureReading(20.0, "1", "1", Instant.now());

        //when
        var anomaly = anomalyDetector.apply(List.of(reading));

        //then
        Assertions.assertNotNull(anomaly);
        Assertions.assertTrue(anomaly.isEmpty());
    }

    @Test
    public void testSimpleCase() {
        //given
        var readings = List.of(
                new TemperatureReading(21.0, "1", "1", Instant.parse("2023-07-30T12:00:00Z")),
                new TemperatureReading(22.0, "1", "1", Instant.parse("2023-07-30T12:00:00Z")),
                new TemperatureReading(23.0, "1", "1", Instant.parse("2023-07-30T12:00:00Z"))
        );

        //when
        var anomaly = anomalyDetector.apply(readings);

        //then
        Assertions.assertNotNull(anomaly);
        Assertions.assertTrue(anomaly.isEmpty());
    }

    @Test
    public void testSimpleCaseAnomaly() {
        //given
        var readings = List.of(
                new TemperatureReading(20.0, "1", "1", Instant.parse("2023-07-30T12:01:00Z")),
                new TemperatureReading(29.3, "1", "1", Instant.parse("2023-07-30T12:02:00Z")),
                new TemperatureReading(20.1, "1", "1", Instant.parse("2023-07-30T12:03:00Z")),
                new TemperatureReading(20.2, "1", "1", Instant.parse("2023-07-30T12:04:00Z")),
                new TemperatureReading(20.3, "1", "1", Instant.parse("2023-07-30T12:05:00Z")),
                new TemperatureReading(20.4, "1", "1", Instant.parse("2023-07-30T12:06:00Z")),
                new TemperatureReading(20.5, "1", "1", Instant.parse("2023-07-30T12:07:00Z")),
                new TemperatureReading(20.6, "1", "1", Instant.parse("2023-07-30T12:08:00Z")),
                new TemperatureReading(20.7, "1", "1", Instant.parse("2023-07-30T12:09:00Z")),
                new TemperatureReading(20.8, "1", "1", Instant.parse("2023-07-30T12:10:00Z"))
        );

        //when
        var anomaly = anomalyDetector.apply(readings);

        //then
        Assertions.assertFalse(anomaly.isEmpty());
        Assertions.assertEquals(1, anomaly.size());
        Assertions.assertEquals(29.3, anomaly.get(0).temperature());
    }

    @Test
    public void testSimpleCaseAnomalies() {
        //given
        var readings = List.of(
                new TemperatureReading(20.0, "1", "1", Instant.parse("2023-07-30T12:01:00Z")),
                new TemperatureReading(29.3, "1", "1", Instant.parse("2023-07-30T12:02:00Z")),
                new TemperatureReading(20.1, "1", "1", Instant.parse("2023-07-30T12:03:00Z")),
                new TemperatureReading(20.2, "1", "1", Instant.parse("2023-07-30T12:04:00Z")),
                new TemperatureReading(20.3, "1", "1", Instant.parse("2023-07-30T12:05:00Z")),
                new TemperatureReading(20.4, "1", "1", Instant.parse("2023-07-30T12:06:00Z")),
                new TemperatureReading(20.5, "1", "1", Instant.parse("2023-07-30T12:07:00Z")),
                new TemperatureReading(20.6, "1", "1", Instant.parse("2023-07-30T12:08:00Z")),
                new TemperatureReading(20.7, "1", "1", Instant.parse("2023-07-30T12:09:00Z")),
                new TemperatureReading(20.8, "1", "1", Instant.parse("2023-07-30T12:10:00Z")),
                new TemperatureReading(20.8, "1", "1", Instant.parse("2023-07-30T12:10:00Z")),
                new TemperatureReading(31.8, "1", "1", Instant.parse("2023-07-30T12:10:00Z"))
        );

        //when
        var anomaly = anomalyDetector.apply(readings);

        //then
        Assertions.assertFalse(anomaly.isEmpty());
        Assertions.assertEquals(2, anomaly.size());
        Assertions.assertEquals(29.3, anomaly.get(0).temperature());
        assertThat(anomaly, containsInAnyOrder(new Anomaly(31.8, "1", "1", Instant.parse("2023-07-30T12:10:00Z")), new Anomaly(29.3, "1", "1", Instant.parse("2023-07-30T12:02:00Z"))));
    }
}
