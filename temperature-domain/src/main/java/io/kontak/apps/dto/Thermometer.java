package io.kontak.apps.dto;

import lombok.Data;

@Data
public class Thermometer {

    private String thermometerId;
    private String roomId;
    private long anomaliesNumber;
}
