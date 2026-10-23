package org.clawlor.weatherapi.service;

import org.clawlor.weatherapi.model.SensorReading;
import org.clawlor.weatherapi.repository.SensorReadingRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class SensorReadingServiceTests {
    private SensorReadingService createService() {
        SensorReadingRepository repository =
                mock(SensorReadingRepository.class);
        return new SensorReadingService(repository);
    }

    private List<SensorReading> createReadings() {
        SensorReading first = new SensorReading();
        first.setTemperature(10.0);
        first.setHumidity(40.0);
        first.setWindSpeed(5.0);

        SensorReading second = new SensorReading();
        second.setTemperature(20.0);
        second.setHumidity(60.0);
        second.setWindSpeed(15.0);

        return List.of(first, second);
    }

    @Test
    void calculatesAverageTemperature() {
        assertEquals(15.0,
                createService().calculateAverageTemperature(createReadings()));
    }

    @Test
    void calculatesMinimumTemperature() {
        assertEquals(10.0,
                createService().calculateMinimumTemperature(createReadings()));
    }

    @Test
    void calculatesMaximumTemperature() {
        assertEquals(20.0,
                createService().calculateMaximumTemperature(createReadings()));
    }

    @Test
    void calculatesSumTemperature() {
        assertEquals(30.0,
                createService().calculateSumTemperature(createReadings()));
    }

    @Test
    void calculatesAverageHumidity() {
        assertEquals(50.0,
                createService().calculateAverageHumidity(createReadings()));
    }

    @Test
    void calculatesAverageWindSpeed() {
        assertEquals(10.0,
                createService().calculateAverageWindSpeed(createReadings()));
    }

}
