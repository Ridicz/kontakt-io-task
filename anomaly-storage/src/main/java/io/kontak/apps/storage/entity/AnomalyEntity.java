package io.kontak.apps.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document
public class AnomalyEntity {

    @Id
    private String id;

    private double temperature;

    private String roomId;
    private String thermometerId;
    private Instant timestamp;
}
