package com.weather.monitor.controller;

import com.weather.monitor.model.WeatherData;
import com.weather.monitor.repository.WeatherDataRepository;
import com.weather.monitor.repository.DailyWeatherSummaryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherDataRepository weatherDataRepository;

    @MockBean
    private DailyWeatherSummaryRepository summaryRepository;

    @Test
    void testGetCurrentWeather() throws Exception {
        WeatherData testData = new WeatherData();
        testData.setCity("Delhi");
        testData.setTemperature(25.0);
        
        when(weatherDataRepository.findAll())
            .thenReturn(Arrays.asList(testData));

        mockMvc.perform(get("/api/weather/current"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].city").value("Delhi"))
            .andExpect(jsonPath("$[0].temperature").value(25.0));
    }
}
