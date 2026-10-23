package org.clawlor.weatherapi.dto;

import java.util.Map;

public class StatisticsResponse {

    private Map<String, Map<String, Double>> results;

    public StatisticsResponse() {
    }

    public StatisticsResponse(Map<String, Map<String, Double>> results) {
        this.results = results;
    }

    public Map<String, Map<String, Double>> getResults() {
        return results;
    }

    public void setResults(Map<String, Map<String, Double>> results) {
        this.results = results;
    }
}