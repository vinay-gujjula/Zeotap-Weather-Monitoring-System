package com.weather.monitor.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

@Entity
@Data
public class DailyWeatherSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String city;
    private LocalDate date;
    private Double avgTemperature;
    private Double maxTemperature;
    private Double minTemperature;
    private String dominantCondition;
}
