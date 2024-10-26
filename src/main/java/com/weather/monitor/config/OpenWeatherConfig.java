package com.weather.monitor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "openweather")
@Data
public class OpenWeatherConfig {
    private String apiKey;
    private String baseUrl;
    private Integer updateInterval;
    private Double alertThreshold;
}
