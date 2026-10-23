package org.clawlor.weatherapi.controller;

import org.clawlor.weatherapi.dto.StatisticsResponse;
import org.clawlor.weatherapi.model.SensorReading;
import org.clawlor.weatherapi.service.SensorReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    public SensorReadingController(SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
    }

    @PostMapping("/sensors/{sensorId}/readings")
    public SensorReading createReading(
            @PathVariable("sensorId") String sensorId,
            @RequestBody SensorReading reading) {
        reading.setSensorId(sensorId);
        return sensorReadingService.saveReading(reading);
    }

    @GetMapping("/sensors/{sensorId}/readings")
    public List<SensorReading> getReadingsBySensorId(
            @PathVariable("sensorId") String sensorId) {
        return sensorReadingService.getReadingsBySensorId(sensorId);
    }

    @GetMapping("/sensors/{sensorId}/statistics")
    public StatisticsResponse getStatistics(
            @PathVariable("sensorId") String sensorId,
            @RequestParam(value = "statistics", required = false) List<String> statistics,
            @RequestParam(value = "metrics", defaultValue = "temperature") List<String> metrics,
            @RequestParam(value = "start", required = false) LocalDateTime start,
            @RequestParam(value = "end", required = false) LocalDateTime end) {

        validateDateRange(start, end);

        List<SensorReading> readings;

        if (start != null) {
            readings = sensorReadingService
                    .getReadingsBySensorIdAndDateRange(sensorId, start, end);
        } else {
            readings = sensorReadingService.getLatestReading(sensorId);
        }

        return calculateStatistics(readings, metrics, statistics);
    }

    @GetMapping("/sensors/statistics")
    public ResponseEntity<Map<String, StatisticsResponse>> getStatisticsForMultipleSensors(
            @RequestParam("sensorIds") List<String> sensorIds,
            @RequestParam(value = "metrics", defaultValue = "temperature") List<String> metrics,
            @RequestParam(value = "statistics", required = false) List<String> statistics,
            @RequestParam(value = "start", required = false) LocalDateTime start,
            @RequestParam(value = "end", required = false) LocalDateTime end) {

        validateDateRange(start, end);

        List<SensorReading> readings;

        if (start != null) {
            readings = sensorReadingService
                    .getReadingsBySensorIdsAndDateRange(sensorIds, start, end);
        } else {
            readings = sensorIds.stream()
                    .map(sensorReadingService::getLatestReading)
                    .flatMap(List::stream)
                    .toList();
        }

        Map<String, StatisticsResponse> results = new LinkedHashMap<>();

        for (String sensorId : sensorIds) {
            List<SensorReading> sensorReadings = readings.stream()
                    .filter(reading -> reading.getSensorId().equals(sensorId))
                    .toList();

            results.put(sensorId,
                    calculateStatistics(sensorReadings, metrics, statistics));
        }

        return ResponseEntity.ok(results);
    }

    private StatisticsResponse calculateStatistics(
            List<SensorReading> readings,
            List<String> metrics,
            List<String> statistics) {

        if (statistics == null || statistics.isEmpty()) {
            statistics = List.of("min", "max", "sum", "average");
        }

        List<String> validStatistics = List.of("min", "max", "sum", "average");

        for (String statistic : statistics) {
            if (validStatistics.stream()
                    .noneMatch(valid -> valid.equalsIgnoreCase(statistic))) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Unsupported statistic: " + statistic);
            }
        }

        Map<String, Map<String, Double>> results = new HashMap<>();

        for (String metric : metrics) {
            Map<String, Double> metricStats = new HashMap<>();

            switch (metric.toLowerCase()) {
                case "temperature":
                    addStatistic(metricStats, statistics, "min",
                            sensorReadingService.calculateMinimumTemperature(readings));
                    addStatistic(metricStats, statistics, "max",
                            sensorReadingService.calculateMaximumTemperature(readings));
                    addStatistic(metricStats, statistics, "sum",
                            sensorReadingService.calculateSumTemperature(readings));
                    addStatistic(metricStats, statistics, "average",
                            sensorReadingService.calculateAverageTemperature(readings));
                    break;

                case "humidity":
                    addStatistic(metricStats, statistics, "min",
                            sensorReadingService.calculateMinimumHumidity(readings));
                    addStatistic(metricStats, statistics, "max",
                            sensorReadingService.calculateMaximumHumidity(readings));
                    addStatistic(metricStats, statistics, "sum",
                            sensorReadingService.calculateSumHumidity(readings));
                    addStatistic(metricStats, statistics, "average",
                            sensorReadingService.calculateAverageHumidity(readings));
                    break;

                case "windspeed":
                    addStatistic(metricStats, statistics, "min",
                            sensorReadingService.calculateMinimumWindSpeed(readings));
                    addStatistic(metricStats, statistics, "max",
                            sensorReadingService.calculateMaximumWindSpeed(readings));
                    addStatistic(metricStats, statistics, "sum",
                            sensorReadingService.calculateSumWindSpeed(readings));
                    addStatistic(metricStats, statistics, "average",
                            sensorReadingService.calculateAverageWindSpeed(readings));
                    break;

                default:
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Unsupported metric: " + metric);
            }

            results.put(metric, metricStats);
        }

        return new StatisticsResponse(results);
    }

    private void addStatistic(
            Map<String, Double> metricStats,
            List<String> requestedStatistics,
            String statistic,
            Double value) {

        if (requestedStatistics.stream()
                .anyMatch(requested -> requested.equalsIgnoreCase(statistic))) {
            metricStats.put(statistic, value);
        }
    }

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if ((start == null && end != null) || (start != null && end == null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Both start and end dates must be provided");
        }

        if (start != null && start.isAfter(end)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date must be before or equal to end date");
        }

        if (start != null && end.isBefore(start.plusDays(1))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Date range must be at least one day");
        }

        if (start != null && start.isBefore(end.minusMonths(1))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Date range must not exceed one month");
        }
    }
}
