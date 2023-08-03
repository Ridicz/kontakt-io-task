package io.kontak.apps.temperature.generator.generator;

import io.kontak.apps.event.TemperatureReading;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FromFileTemperatureGenerator implements TemperatureGenerator {

    private static final String COMMA_DELIMITER = ",";
    private static final int TEMPERATURE_INDEX = 0;
    private static final int ROOM_ID_INDEX = 1;
    private static final int THERMOMETER_ID_INDEX = 2;
    private static final int TIMESTAMP_INDEX = 3;

    @Value("${sample-temperatures.path}")
    private String temperaturesFilePath;

    @Override
    public List<TemperatureReading> generate() {
        try {
            File temperaturesFile = new ClassPathResource(temperaturesFilePath).getFile();
            return loadFromFile(temperaturesFile.toPath());
        } catch (IOException e) {
            log.error("Loading temperatures file failed", e);
            return new SimpleTemperatureGenerator().generate();
        }
    }

    private List<TemperatureReading> loadFromFile(Path path) throws IOException {
        var collect = Files.lines(path)
                .map(this::parseTemperatureReading)
                .collect(Collectors.toList());

        log.info("Loaded {} sample temperature readings from file", collect.size());

        return collect;
    }

    private TemperatureReading parseTemperatureReading(String line) {
        String[] vars = line.split(COMMA_DELIMITER);

        return new TemperatureReading(Double.parseDouble(vars[TEMPERATURE_INDEX]), vars[ROOM_ID_INDEX], vars[THERMOMETER_ID_INDEX], Instant.parse(vars[TIMESTAMP_INDEX]));
    }
}
