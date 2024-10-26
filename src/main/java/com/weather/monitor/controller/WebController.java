package com.weather.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.weather.monitor.service.WeatherService;

import lombok.RequiredArgsConstructor;
import java.util.Arrays; // Importing Arrays to use for the list of cities

@Controller
@RequiredArgsConstructor
public class WebController {
    private final WeatherService weatherService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("cities", Arrays.asList("Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad"));
        return "dashboard"; // Returns the "dashboard" view
    }
}
