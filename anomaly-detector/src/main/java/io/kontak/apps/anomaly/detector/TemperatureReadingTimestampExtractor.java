package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.time.Instant;
import java.util.Optional;

public class TemperatureReadingTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        var reading = (TemperatureReading) record.value();

        return Optional.ofNullable(reading.timestamp())
                .map(Instant::toEpochMilli)
                .orElse(partitionTime);
    }
}
