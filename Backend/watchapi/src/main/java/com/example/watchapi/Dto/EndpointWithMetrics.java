package com.example.watchapi.Dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EndpointWithMetrics {
    private String method;
    private String path;
    private Map<String, Object> metrics;
}
