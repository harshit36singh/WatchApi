package com.example.watchapi.Dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpTraceEntry {
    private Instant timestamp;
    private String method;
    private String uri;
    private int status;
    private long timeTakenMs;
}
