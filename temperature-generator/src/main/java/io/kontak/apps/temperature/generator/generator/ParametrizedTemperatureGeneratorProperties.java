package io.kontak.apps.temperature.generator.generator;

import io.kontak.apps.dto.GeneratorParams;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class ParametrizedTemperatureGeneratorProperties {
    private Map<String, GeneratorParams> config;
}
