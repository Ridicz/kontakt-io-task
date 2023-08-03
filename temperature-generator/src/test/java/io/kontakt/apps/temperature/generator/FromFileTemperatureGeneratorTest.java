package io.kontakt.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import io.kontak.apps.temperature.generator.generator.FromFileTemperatureGenerator;
import io.kontak.apps.temperature.generator.TemperatureGeneratorApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;

@SpringBootTest(classes = TemperatureGeneratorApplication.class)
@TestPropertySource(properties = "sample-temperatures.path=test-temperatures.csv")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FromFileTemperatureGeneratorTest {

    @Autowired
    private FromFileTemperatureGenerator fromFileTemperatureGenerator;

    @Test
    public void testLoadingTemperaturesFromFile() {
        //given
        //when
        var generatedTemperatures = fromFileTemperatureGenerator.generate();

        //then
        Assertions.assertNotNull(generatedTemperatures);
        Assertions.assertEquals(3, generatedTemperatures.size());
        Assertions.assertEquals(new TemperatureReading(21.11, "1", "1", Instant.parse("2023-01-01T00:00:00Z")), generatedTemperatures.get(0));
        Assertions.assertEquals(new TemperatureReading(22.21, "2", "2", Instant.parse("2023-01-01T01:00:00Z")), generatedTemperatures.get(1));
        Assertions.assertEquals(new TemperatureReading(23.13, "3", "3", Instant.parse("2023-01-01T02:00:00Z")), generatedTemperatures.get(2));
    }
}
