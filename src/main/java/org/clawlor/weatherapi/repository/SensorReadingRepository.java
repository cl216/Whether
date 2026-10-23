package org.clawlor.weatherapi.repository;

import org.clawlor.weatherapi.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorReadingRepository
        extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findBySensorId(String sensorId);

List<SensorReading> findBySensorIdAndRecordedAtBetween(
        String sensorId,
        LocalDateTime start,
        LocalDateTime end
);

    List<SensorReading> findBySensorIdInAndRecordedAtBetween(
            List<String> sensorIds,
            LocalDateTime start,
            LocalDateTime end
    );

    List<SensorReading> findBySensorIdOrderByRecordedAtDesc(String sensorId);
}