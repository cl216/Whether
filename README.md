# Weather API

A Java Spring Boot API for submitting weather sensor readings and getting statistics.

## Running

Run `WeatherApiApplication` in IDE, or use:

```bash
mvn spring-boot:run
```

## Swagger UI

With the application running, open:

http://localhost:8080/swagger-ui/index.html

Use this to view the endpoints and send requests.

## Testing

Run the tests using the Maven `test` goal in IntelliJ.

You can also use `test-reading.http` to send API requests from IntelliJ.

## Endpoints

- `POST /sensors/{sensorId}/readings` — submit a reading
- `GET /sensors/{sensorId}/readings` — get readings for a sensor
- `GET /sensors/{sensorId}/statistics` — get statistics for one sensor
- `GET /sensors/statistics?sensorIds=sensor-101,sensor-102` — get statistics for multiple sensors

Statistics support temperature, humidity and wind speed, with min, max, sum and average calculations. Date ranges can also be supplied using `start` and `end`.

## Database

Uses H2 with file-based storage to keep readings between restarts.
