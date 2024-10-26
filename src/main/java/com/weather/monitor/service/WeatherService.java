package com.weather.monitor.service;

import com.weather.monitor.config.OpenWeatherConfig;
import com.weather.monitor.model.WeatherData;
import com.weather.monitor.model.DailyWeatherSummary;
import com.weather.monitor.repository.WeatherDataRepository;
import com.weather.monitor.repository.DailyWeatherSummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    
    private final OpenWeatherConfig config;
    private final WeatherDataRepository weatherDataRepository;
    private final DailyWeatherSummaryRepository summaryRepository;
    private final RestTemplate restTemplate;
    
    private static final List<String> CITIES = Arrays.asList(
        "Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad"
    );

    @Autowired
    public WeatherService(OpenWeatherConfig config, 
                         WeatherDataRepository weatherDataRepository,
                         DailyWeatherSummaryRepository summaryRepository,
                         RestTemplate restTemplate) {
        this.config = config;
        this.weatherDataRepository = weatherDataRepository;
        this.summaryRepository = summaryRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRateString = "${openweather.update-interval}")
    public void fetchWeatherData() {
        for (String city : CITIES) {
            try {
                String url = String.format("%s/weather?q=%s,IN&appid=%s&units=metric",
                    config.getBaseUrl(), city, config.getApiKey());
                
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                
                if (response != null) {
                    WeatherData weatherData = parseWeatherData(response, city);
                    weatherDataRepository.save(weatherData);
                    checkThresholdAndAlert(weatherData);
                    updateDailySummary(city);
                }
            } catch (Exception e) {
                log.error("Error fetching weather data for {}: {}", city, e.getMessage());
            }
        }
    }

    private WeatherData parseWeatherData(Map<String, Object> response, String city) {
        WeatherData data = new WeatherData();
        data.setCity(city);
        data.setTimestamp(LocalDateTime.now());
        
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        List<Map<String, Object>> weather = (List<Map<String, Object>>) response.get("weather");
        
        data.setTemperature(((Number) main.get("temp")).doubleValue());
        data.setFeelsLike(((Number) main.get("feels_like")).doubleValue());
        data.setMainCondition((String) weather.get(0).get("main"));
        
        return data;
    }

    private void checkThresholdAndAlert(WeatherData data) {
        if (data.getTemperature() > config.getAlertThreshold()) {
            log.warn("ALERT: Temperature in {} is {}, exceeding threshold of {}",
                data.getCity(), data.getTemperature(), config.getAlertThreshold());
        }
    }

    private void updateDailySummary(String city) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<WeatherData> dayData = weatherDataRepository.findByCityAndTimestampBetween(
            city, startOfDay, LocalDateTime.now());
        
        if (!dayData.isEmpty()) {
            DailyWeatherSummary summary = new DailyWeatherSummary();
            summary.setCity(city);
            summary.setDate(LocalDate.now());
            
            DoubleSummaryStatistics stats = dayData.stream()
                .mapToDouble(WeatherData::getTemperature)
                .summaryStatistics();
            
            summary.setAvgTemperature(stats.getAverage());
            summary.setMaxTemperature(stats.getMax());
            summary.setMinTemperature(stats.getMin());
            
            Map<String, Long> conditionCounts = dayData.stream()
                .collect(Collectors.groupingBy(
                    data -> data.getMainCondition(),
                    Collectors.counting()
                ));
            
            String dominantCondition = conditionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
            
            summary.setDominantCondition(dominantCondition);
            summaryRepository.save(summary);
        }
    }
}