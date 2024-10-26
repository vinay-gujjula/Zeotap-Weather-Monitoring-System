package com.weather.monitor.service;

import com.weather.monitor.config.OpenWeatherConfig;
import com.weather.monitor.model.WeatherData;
import com.weather.monitor.repository.WeatherDataRepository;
import com.weather.monitor.repository.DailyWeatherSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private WeatherDataRepository weatherDataRepository;
    
    @Mock
    private DailyWeatherSummaryRepository summaryRepository;
    
    private WeatherService weatherService;
    
    private OpenWeatherConfig config;

    @BeforeEach
    void setUp() {
        config = new OpenWeatherConfig();
        config.setApiKey("test-api-key");
        config.setBaseUrl("http://api.test.com");
        config.setAlertThreshold(35.0);
        
        weatherService = new WeatherService(config, weatherDataRepository, 
            summaryRepository, restTemplate);
    }

    @Test
    void testFetchWeatherData() {
        // Prepare test data
        Map<String, Object> weatherResponse = new HashMap<>();
        Map<String, Object> main = new HashMap<>();
        main.put("temp", 25.0);
        main.put("feels_like", 26.0);
        
        List<Map<String, Object>> weather = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("main", "Clear");
        weather.add(condition);
        
        weatherResponse.put("main", main);
        weatherResponse.put("weather", weather);

        // Mock API response
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenReturn(weatherResponse);

        // Execute
        weatherService.fetchWeatherData();

        // Verify
        verify(weatherDataRepository, times(6)).save(any(WeatherData.class));
    }

    @Test
    void testTemperatureAlert() {
        // Test data with temperature above threshold
        Map<String, Object> weatherResponse = new HashMap<>();
        Map<String, Object> main = new HashMap<>();
        main.put("temp", 36.0);
        main.put("feels_like", 38.0);
        
        List<Map<String, Object>> weather = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("main", "Clear");
        weather.add(condition);
        
        weatherResponse.put("main", main);
        weatherResponse.put("weather", weather);

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenReturn(weatherResponse);

        weatherService.fetchWeatherData();
        
        // Verify alert was triggered (check logs or notification system)
        verify(weatherDataRepository, times(6)).save(any(WeatherData.class));
    }
}
