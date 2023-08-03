package io.kontak.apps.anomaly.detector.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "anomaly.detector", name = "impl", havingValue = "meanAnomalyDetector")
public class MeanAnomalyDetector implements AnomalyDetector {

    private static final Duration ANOMALY_WINDOW_DURATION = Duration.ofSeconds(10L);

    @Value("${anomaly.detector.mean.threshold:5.0}")
    private double anomalyThreshold;

    @Value("${anomaly.detector.mean.sample.size:10}")
    private int sampleSize;

    @Override
    public Duration anomalyWindowDuration() {
        return ANOMALY_WINDOW_DURATION;
    }

    @Override
    public List<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        if (temperatureReadings.size() < sampleSize) {
            log.info("Sample too small: {}, no anomalies", temperatureReadings.size());
            return List.of();
        }

        Set<Anomaly> anomalies = new HashSet<>();

        int startIndex = 0;
        var size = temperatureReadings.size();

        while (startIndex + sampleSize <= size) {
            anomalies.addAll(
                    detectInSample(temperatureReadings, startIndex)
            );
            startIndex++;
        }

        log.info("Detected {} anomalies", anomalies.size());
        return anomalies.stream().toList();
    }

    private Set<Anomaly> detectInSample(List<TemperatureReading> temperatureReadings, int startIndex) {
        Set<Anomaly> anomalies = new HashSet<>();
        int endIndex = startIndex + sampleSize;

        for (int anomalyCandidate = startIndex; anomalyCandidate < endIndex; anomalyCandidate++) {

            detectAnomaly(startIndex, endIndex, anomalyCandidate, temperatureReadings)
                    .ifPresent(anomalies::add);
        }

        return anomalies;
    }

    private Optional<Anomaly> detectAnomaly(int startIndex, int endIndex, int anomalyCandidate, List<TemperatureReading> temperatureReadings) {
        double temperaturesSum = 0;

        for (int currenIndex = startIndex; currenIndex < endIndex; currenIndex++) {
            if (currenIndex != anomalyCandidate) {
                temperaturesSum += temperatureReadings.get(currenIndex).temperature();
            }
        }

        double temperaturesAverage = temperaturesSum / (sampleSize - 1);
        if (temperatureReadings.get(anomalyCandidate).temperature() > temperaturesAverage + anomalyThreshold) {
            var anomalyReading = temperatureReadings.get(anomalyCandidate);
            return Optional.of(new Anomaly(anomalyReading.temperature(), anomalyReading.roomId(), anomalyReading.thermometerId(), anomalyReading.timestamp()));
        }

        return Optional.empty();
    }
}
