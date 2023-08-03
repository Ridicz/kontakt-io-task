package io.kontak.apps.anomaly.detector.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "anomaly.detector", name = "impl", havingValue = "averageAnomalyDetector")
public class AverageAnomalyDetector implements AnomalyDetector {

    private static final Duration ANOMALY_WINDOW_DURATION = Duration.ofSeconds(10L);

    @Value("${anomaly.detector.average.threshold:5.0}")
    private double anomalyThreshold;

    @Override
    public Duration anomalyWindowDuration() {
        return ANOMALY_WINDOW_DURATION;
    }

    @Override
    public List<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        if (temperatureReadings.size() < 2) {
            return List.of();
        }

        var readingsMean = calculateMean(temperatureReadings);

        var anomalies = temperatureReadings
                .stream()
                .filter(reading -> isAnomaly(reading, readingsMean))
                .map(reading -> new Anomaly(reading.temperature(), reading.roomId(), reading.thermometerId(), reading.timestamp()))
                .collect(Collectors.toList());

        log.info("Detected {} anomalies", anomalies.size());
        return anomalies;
    }

    private double calculateMean(List<TemperatureReading> temperatureReadings) {
        return temperatureReadings
                .stream()
                .map(TemperatureReading::temperature)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private boolean isAnomaly(TemperatureReading temperatureReading, double mean) {
        return temperatureReading.temperature() > mean + anomalyThreshold ||
                temperatureReading.temperature() < mean - anomalyThreshold;
    }
}
