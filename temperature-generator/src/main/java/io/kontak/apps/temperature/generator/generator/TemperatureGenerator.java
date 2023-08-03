package io.kontak.apps.temperature.generator.generator;

import io.kontak.apps.event.TemperatureReading;

import java.util.List;

public interface TemperatureGenerator {

    List<TemperatureReading> generate();
}
