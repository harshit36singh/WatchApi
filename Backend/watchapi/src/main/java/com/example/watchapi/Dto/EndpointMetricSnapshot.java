package com.example.watchapi.Dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EndpointMetricSnapshot {
      private Instant timestamp;
    private String controller;
    private String method;
    private String path;
    private double totalRequests;
}
