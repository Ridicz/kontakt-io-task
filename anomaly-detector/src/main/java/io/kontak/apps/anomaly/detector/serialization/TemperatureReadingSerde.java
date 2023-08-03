package io.kontak.apps.anomaly.detector.serialization;

import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class TemperatureReadingSerde implements Serde<TemperatureReading> {

    @Override
    public Serializer<TemperatureReading> serializer() {
        return new JsonSerializer<>();
    }

    @Override
    public Deserializer<TemperatureReading> deserializer() {
        return new JsonDeserializer<>(TemperatureReading.class);
    }
}
