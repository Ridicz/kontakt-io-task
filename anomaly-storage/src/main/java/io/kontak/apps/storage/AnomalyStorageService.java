package io.kontak.apps.storage;

import io.kontak.apps.dto.Thermometer;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.storage.entity.AnomalyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnomalyStorageService {

    private final MongoTemplate mongoTemplate;

    @Value("${threshold:10}")
    private Long threshold;

    private final AnomalyRepository anomalyRepository;

    public void save(Anomaly anomaly) {
        AnomalyEntity anomalyEntity = AnomalyEntity.builder()
                .temperature(anomaly.temperature())
                .thermometerId(anomaly.thermometerId())
                .roomId(anomaly.roomId())
                .timestamp(anomaly.timestamp())
                .build();

        AnomalyEntity save = anomalyRepository.save(anomalyEntity);
        log.info("Saved AnomalyEntity with id: {}", save.getId());
    }

    public Page<Anomaly> getAnomaliesPerThermometer(String thermometerId, Pageable pageable) {
        return anomalyRepository.findByThermometerId(thermometerId, pageable);
    }

    public Page<Anomaly> getAnomaliesPerRoom(String roomId, Pageable pageable) {
        return anomalyRepository.findByRoomId(roomId, pageable);
    }

    public List<Thermometer> getThermometersWithAnomaliesThreshold(Optional<Long> threshold) {
        GroupOperation groupOperation = Aggregation
                .group("thermometerId").count().as("anomaliesNumber")
                .first("thermometerId").as("thermometerId")
                .first("roomId").as("roomId");

        var matchOperation = Aggregation.match(Criteria.where("anomaliesNumber").gt(threshold.orElse(this.threshold)));
        var thermometerAggregation = Aggregation.newAggregation(groupOperation, matchOperation);


        return mongoTemplate.aggregate(thermometerAggregation, AnomalyEntity.class, Thermometer.class).getMappedResults();
    }
}
