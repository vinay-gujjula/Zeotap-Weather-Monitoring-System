package com.weather.monitor.repository;

import com.weather.monitor.model.DailyWeatherSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface DailyWeatherSummaryRepository extends JpaRepository<DailyWeatherSummary, Long> {
    DailyWeatherSummary findByCityAndDate(String city, LocalDate date);
} 
