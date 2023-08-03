package io.kontak.apps.api;

import io.kontak.apps.dto.Thermometer;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.storage.AnomalyStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AnomalyController {

    private final AnomalyStorageService anomalyStorageService;

    @GetMapping(value = "/thermometers/{thermometerID}/anomalies")
    public ResponseEntity<Page<Anomaly>> getAnomaliesPerThermometer(
            @PathVariable("thermometerID") String thermometerID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(anomalyStorageService.getAnomaliesPerThermometer(thermometerID, pageable));
    }

    @GetMapping(value = "/rooms/{roomID}/anomalies")
    public ResponseEntity<Page<Anomaly>> getAnomaliesPerRoom(
            @PathVariable("roomID") String roomID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(anomalyStorageService.getAnomaliesPerRoom(roomID, pageable));
    }

    @GetMapping(value = "/thermometers/threshold")
    public ResponseEntity<List<Thermometer>> getThermometersWithMoreAnomaliesThan(
            @RequestParam(name = "threshold", required = false) Optional<Long> threshold) {

        return ResponseEntity.ok(anomalyStorageService.getThermometersWithAnomaliesThreshold(threshold));
    }
}
