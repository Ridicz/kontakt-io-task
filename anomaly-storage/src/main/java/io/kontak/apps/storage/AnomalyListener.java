package io.kontak.apps.storage;

import io.kontak.apps.event.Anomaly;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnomalyListener {

    private final AnomalyStorageService anomalyStorageService;

    @KafkaListener(topics = "temperature-anomalies", groupId = "temperature-anomalies")
    public void consume(Message<Anomaly> message) {
        log.info("Received anomaly message: {}", message);
        anomalyStorageService.save(message.getPayload());
    }
}
