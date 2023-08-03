package io.kontak.apps.dto;

public record GeneratorParams(int perGeneration,
                              double standardDeviation,
                              double anomalyDeviation,
                              double baseTemp,
                              int anomalyFreq) {
}
