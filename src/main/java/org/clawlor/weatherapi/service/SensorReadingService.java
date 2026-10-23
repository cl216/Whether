package org.clawlor.weatherapi.service;

import org.clawlor.weatherapi.model.SensorReading;
import org.clawlor.weatherapi.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public SensorReading saveReading(SensorReading  reading){
        return sensorReadingRepository.save(reading);
    }

    public List<SensorReading> getAllReadings(){
        return sensorReadingRepository.findAll();
    }

    public List<SensorReading> getReadingsBySensorId(String sensorId) {
        return sensorReadingRepository.findBySensorId(sensorId);
    }

    public SensorReading getReadingById(Long id){
        return sensorReadingRepository.findById(id).orElse(null);
    }

    public List<SensorReading> getReadingsBySensorIdAndDateRange(
            String sensorId,
            LocalDateTime start,
            LocalDateTime end)
    {
        return sensorReadingRepository.findBySensorIdAndRecordedAtBetween(sensorId, start, end);
    }

    public List<SensorReading> getReadingsBySensorIdsAndDateRange(
            List<String> sensorIds,
            LocalDateTime start,
            LocalDateTime end) {

        return sensorReadingRepository
                .findBySensorIdInAndRecordedAtBetween(sensorIds, start, end);
    }


    public Double calculateAverageTemperature(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getTemperature)
                .filter(temperature -> temperature != null)
                .mapToDouble(Double::doubleValue)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }


    public Double calculateAverageHumidity(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getHumidity)
                .filter(humidity -> humidity != null)
                .mapToDouble(Double::doubleValue)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }


    public Double calculateAverageWindSpeed(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getWindSpeed)
                .filter(windSpeed -> windSpeed != null)
                .mapToDouble(Double::doubleValue)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }


    public Double calculateMinimumTemperature(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getTemperature)
                .filter(temperature -> temperature != null)
                .mapToDouble(Double::doubleValue)
                .min()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }


    public Double calculateMaximumTemperature(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getTemperature)
                .filter(temperature -> temperature != null)
                .mapToDouble(Double::doubleValue)
                .max()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }


    public Double calculateSumTemperature(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getTemperature)
                .filter(temperature -> temperature != null)
                .mapToDouble(Double::doubleValue)
                .sum();
    }


    public Double calculateMinimumHumidity(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getHumidity)
                .filter(humidity -> humidity != null)
                .mapToDouble(Double::doubleValue)
                .min()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }


    public Double calculateMaximumHumidity(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getHumidity)
                .filter(humidity -> humidity != null)
                .mapToDouble(Double::doubleValue)
                .max()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }

    public Double calculateSumHumidity(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getHumidity)
                .filter(humidity -> humidity != null)
                .mapToDouble(Double::doubleValue)
                .sum();
    }


    public Double calculateMinimumWindSpeed(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getWindSpeed)
                .filter(windSpeed -> windSpeed != null)
                .mapToDouble(Double::doubleValue)
                .min()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }


    public Double calculateMaximumWindSpeed(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getWindSpeed)
                .filter(windSpeed -> windSpeed != null)
                .mapToDouble(Double::doubleValue)
                .max()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }


    public Double calculateSumWindSpeed(List<SensorReading> readings) {
        return readings.stream()
                .map(SensorReading::getWindSpeed)
                .filter(windSpeed -> windSpeed != null)
                .mapToDouble(Double::doubleValue)
                .sum();
    }


    public void deleteReading(Long id) {
        sensorReadingRepository.deleteById(id);
    }

    public List<SensorReading> getLatestReading(String sensorId) {
        List<SensorReading> readings =
                sensorReadingRepository.findBySensorIdOrderByRecordedAtDesc(sensorId);

        if (readings.isEmpty()) {
            return List.of();
        }

        return List.of(readings.get(0));
    }













}