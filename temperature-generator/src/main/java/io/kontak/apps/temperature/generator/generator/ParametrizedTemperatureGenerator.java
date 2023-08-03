package io.kontak.apps.temperature.generator.generator;

import io.kontak.apps.dto.GeneratorParams;
import io.kontak.apps.event.TemperatureReading;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class ParametrizedTemperatureGenerator implements TemperatureGenerator {

    private static final long TIME_STEP_MILLIS = 10L;

    private final Random random = new Random();

    private final ParametrizedTemperatureGeneratorProperties properties;

    @Override
    public List<TemperatureReading> generate() {
        var temperatureReadings = properties.getConfig().entrySet()
                .stream()
                .map(entry -> createForParams(entry.getValue(), entry.getKey(), entry.getKey()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        log.info("Generated {} temperature readings", temperatureReadings.size());

        return temperatureReadings;
    }

    private List<TemperatureReading> createForParams(GeneratorParams params, String roomId, String thermometerId) {
        int anomalyCounter = 0;
        List<TemperatureReading> readings = new ArrayList<>();
        Instant startTimestamp = Instant.now();

        for (int i = 0; i < params.perGeneration(); i++) {
            double temperature;

            if (anomalyCounter == params.anomalyFreq()) {
                temperature = params.baseTemp() + randomSign(random.nextDouble(0.5, 1.0) * params.anomalyDeviation());
                anomalyCounter = 0;
            } else {
                temperature = params.baseTemp() + random.nextDouble(-params.standardDeviation(), params.standardDeviation());
                anomalyCounter++;
            }

            readings.add(new TemperatureReading(temperature, roomId, thermometerId, startTimestamp));


            startTimestamp = startTimestamp.plusMillis(TIME_STEP_MILLIS);
        }

        return readings;
    }

    private double randomSign(double val) {
        return val * (random.nextBoolean() ? 1 : -1);
    }
}
