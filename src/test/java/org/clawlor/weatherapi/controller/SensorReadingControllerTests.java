package org.clawlor.weatherapi.controller;

import org.clawlor.weatherapi.service.SensorReadingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorReadingController.class)
class SensorReadingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorReadingService sensorReadingService;

    @Test
    void validStatisticsRequestReturnsOk() throws Exception {
        mockMvc.perform(get("/sensors/sensor-101/statistics")
                        .param("metrics", "temperature"))
                .andExpect(status().isOk());
    }

    @Test
    void invalidDateRangeReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/sensors/sensor-101/statistics")
                        .param("start", "2026-10-23T00:00:00")
                        .param("end", "2026-10-22T00:00:00"))
                .andExpect(status().isBadRequest());
    }
}
