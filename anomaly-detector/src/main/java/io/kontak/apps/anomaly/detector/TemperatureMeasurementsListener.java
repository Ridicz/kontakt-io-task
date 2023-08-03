package io.kontak.apps.anomaly.detector;

import io.kontak.apps.anomaly.detector.detector.AnomalyDetector;
import io.kontak.apps.anomaly.detector.serialization.ArrayListSerde;
import io.kontak.apps.anomaly.detector.serialization.TemperatureReadingSerde;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Function;

@Slf4j
public class TemperatureMeasurementsListener implements Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> {

    private final AnomalyDetector anomalyDetector;

    public TemperatureMeasurementsListener(AnomalyDetector anomalyDetector) {
        this.anomalyDetector = anomalyDetector;
    }

    @Override
    public KStream<String, Anomaly> apply(KStream<String, TemperatureReading> events) {
        return events
                .groupBy((key, reading) -> reading.thermometerId(), Grouped.with(new Serdes.StringSerde(), new TemperatureReadingSerde()))
                .windowedBy(TimeWindows.ofSizeAndGrace(anomalyDetector.anomalyWindowDuration(), Duration.ofSeconds(1L)))
                .aggregate((Initializer<ArrayList<TemperatureReading>>) ArrayList::new, (key, value, aggregate) -> {
                    aggregate.add(value);
                    return aggregate;
                }, Materialized.with(new Serdes.StringSerde(), new ArrayListSerde<>(new TemperatureReadingSerde())))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .mapValues((key, value) -> anomalyDetector.apply(value))
                .filter((s, anomaly) -> !anomaly.isEmpty())
                .flatMapValues((stringWindowed, anomalies) -> anomalies)
                .selectKey((s, anomaly) -> anomaly.thermometerId());
    }
}
