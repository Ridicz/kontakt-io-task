package io.kontak.apps.storage;

import io.kontak.apps.storage.entity.AnomalyEntity;
import io.kontak.apps.event.Anomaly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnomalyRepository extends MongoRepository<AnomalyEntity, Long> {

    Page<Anomaly> findByThermometerId(String thermometerId, Pageable pageable);

    Page<Anomaly> findByRoomId(String roomId, Pageable pageable);
}
