# Zeotap-Weather-Monitoring-System
Weather monitoring system for real-time data processing and alerting
# Real-Time Weather Monitoring System

A Spring Boot application that provides real-time weather monitoring for major Indian cities, featuring data aggregation, daily summaries, and temperature alerts.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Design Overview](#design-overview)
- [Testing](#testing)
- [Contributing](#contributing)

## Features

- Real-time weather data monitoring for 6 major Indian cities
  - Delhi, Mumbai, Chennai, Bangalore, Kolkata, Hyderabad
- Automated data collection from OpenWeatherMap API
- Daily weather summaries with aggregated statistics
- Temperature threshold alerts
- H2 database for data persistence
- Web dashboard for visualization
- RESTful API endpoints for data access

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Lombok
- JUnit 5
- OpenWeatherMap API
- Spring Scheduler
- Thymeleaf (for dashboard)

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6.x or higher
- OpenWeatherMap API key
- Git (optional)

## Installation

1. Clone the repository:
bash
git clone https://github.com/vinay-gujjula/Zeotap-Weather-Monitoring-System.git
cd weather-monitoring-system
```

2. Build the project:
Bash
mvn clean install
```

3. Run the application:
Bash
java -jar target/weather-monitor-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Configuration

The application uses `application.properties` for configuration:

properties
# Server Configuration
server.port=8080

# OpenWeather API Configuration
openweather.api-key=your_api_key_here
openweather.base-url=https://api.openweathermap.org/data/2.5
openweather.update-interval=300000
openweather.alert-threshold=35.0

# Database Configuration
spring.datasource.url=jdbc:h2:file:./weatherdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Logging
logging.level.com.weather.monitor=INFO
logging.file.name=weather-monitor.log
```

## API Documentation

### REST Endpoints

1. Get Current Weather Data
HTTP
GET /api/weather/current

Returns current weather data for all monitored cities.

2. Get Daily Summaries
HTTP
GET /api/weather/summary
```
Returns aggregated daily weather summaries.

### Web Interface

Access the dashboard at:
HTTP
GET /
```
Displays weather information for all monitored cities.

## Project Structure

```
src/main/java/com/weather/monitor/
├── config/
│   ├── OpenWeatherConfig.java
│   └── RestTemplateConfig.java
├── controller/
│   ├── WeatherController.java
│   └── WebController.java
├── model/
│   ├── WeatherData.java
│   └── DailyWeatherSummary.java
├── repository/
│   ├── WeatherDataRepository.java
│   └── DailyWeatherSummaryRepository.java
└── service/
    └── WeatherService.java
```

## Design Overview

### Data Models

1. WeatherData Entity:
java
- Long id
- String city
- String mainCondition
- Double temperature
- Double feelsLike
- LocalDateTime timestamp
```

2. DailyWeatherSummary Entity:
java
- Long id
- String city
- LocalDate date
- Double avgTemperature
- Double maxTemperature
- Double minTemperature
- String dominantCondition
```

### Key Components

1. **WeatherService**:
   - Scheduled weather data fetching
   - Temperature threshold monitoring
   - Daily summary calculation
   - Dominant weather condition determination

2. **OpenWeatherConfig**:
   - API configuration management
   - Update interval settings
   - Alert threshold configuration

3. **Repositories**:
   - JPA repositories for data persistence
   - Custom query methods for data retrieval

## Testing

The project includes both unit and integration tests:

1. Run tests:
Bash
mvn test
```

2. Test coverage includes:
   - WeatherController endpoints
   - WeatherService functionality
   - Data parsing and processing
   - Alert threshold monitoring

Key test files:
- `WeatherControllerTest.java`
- `WeatherServiceTest.java`

## Monitoring and Alerts

1. Temperature Alerts:
   - Configured threshold: 35.0°C
   - Alerts logged when threshold exceeded
   - Customizable alert threshold in properties

2. Data Collection:
   - 5-minute update interval (configurable)
   - Automatic retry on API failures
   - Error logging and monitoring

## Database Access

H2 Console is enabled for development:
1. Access: `http://localhost:8080/h2-console`
2. JDBC URL: `jdbc:h2:file:./weatherdb`
3. Username: `sa`
4. Password: `password`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.
