package com.weather.monitor.controller;

import com.weather.monitor.model.DailyWeatherSummary;
import com.weather.monitor.model.WeatherData;
import com.weather.monitor.repository.DailyWeatherSummaryRepository;
import com.weather.monitor.repository.WeatherDataRepository;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherDataRepository weatherDataRepository;
    private final DailyWeatherSummaryRepository summaryRepository;

    @GetMapping("/current")
    public List<WeatherData> getCurrentWeather() {
        return weatherDataRepository.findAll();
    }

    @GetMapping("/summary")
    public List<DailyWeatherSummary> getDailySummaries() {
        return summaryRepository.findAll();
    }
}
