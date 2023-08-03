package io.kontakt.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TemperatureMeasurementsListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-in-0.destination}")
    private String inputTopic;

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-out-0.destination}")
    private String outputTopic;

    @Test
    void testInOutFlow() throws InterruptedException {
        try (TestKafkaConsumer<Anomaly> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
                Anomaly.class
        );
             TestKafkaProducer<TemperatureReading> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {

            String thermometerId = "thermometer";

            for (int i = 0; i < 12; i++) {
                IntStream.range(0, 10)
                        .mapToObj(operand -> new TemperatureReading(20.0, "room", thermometerId, Instant.now().plusMillis(operand)))
                        .forEach(reading -> producer.produce(thermometerId, reading));

                producer.produce(thermometerId, new TemperatureReading(30.0, "room", thermometerId, Instant.now().plusMillis(20L)));

                Thread.sleep(1000L);
            }

            consumer.drain(
                    consumerRecords -> consumerRecords.stream().anyMatch(r -> r.value().thermometerId().equals(thermometerId)),
                    Duration.ofSeconds(40L)//Kafka commit interval is 30s, wait time should be longer than that
            );
        }
    }
}
