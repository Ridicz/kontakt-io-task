package io.kontak.apps.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"io.kontak.apps.storage", "io.kontak.apps.api"})
public class TemperatureAnalyticsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemperatureAnalyticsApiApplication.class, args);
    }
}
