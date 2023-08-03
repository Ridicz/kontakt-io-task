package io.kontak.apps.storage.config;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import io.kontak.apps.storage.AnomalyListener;
import io.kontak.apps.storage.AnomalyRepository;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class KafkaConfig {

//    @Bean
//    public Function<KStream<String, Anomaly>, Anomaly> anomalyStorage(AnomalyRepository anomalyRepository) {
//        return new AnomalyListener(anomalyRepository);
//    }

    @Bean
    public KStream<String, Anomaly> anomalyStorage() {
        StreamsBuilder builder = new StreamsBuilder();
        return builder.stream("temperature-anomalies");
    }
}
