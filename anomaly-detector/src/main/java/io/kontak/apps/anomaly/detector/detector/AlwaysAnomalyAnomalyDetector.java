package io.kontak.apps.anomaly.detector.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "anomaly.detector", name = "impl", havingValue = "alwaysAnomalyAnomalyDetector")
public class AlwaysAnomalyAnomalyDetector implements AnomalyDetector {

    private static final Duration ANOMALY_WINDOW_DURATION = Duration.ofSeconds(10L);

    @Override
    public Duration anomalyWindowDuration() {
        return ANOMALY_WINDOW_DURATION;
    }

    @Override
    public List<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        TemperatureReading temperatureReading = temperatureReadings.get(0);
        return List.of(new Anomaly(
                temperatureReading.temperature(),
                temperatureReading.roomId(),
                temperatureReading.thermometerId(),
                temperatureReading.timestamp()
        ));
    }
}
