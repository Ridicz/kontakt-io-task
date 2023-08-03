package io.kontak.apps.anomaly.detector.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public interface AnomalyDetector extends Function<List<TemperatureReading>, List<Anomaly>> {

    Duration anomalyWindowDuration();
}
