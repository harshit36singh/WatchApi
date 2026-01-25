package com.example.watchapi.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.watchapi.Dto.EndpointMetricSnapshot;

@Component
public class EndpointMetricsWindow {

    private final Deque<EndpointMetricSnapshot> window = new LinkedList<>();
    private static final Duration WINDOW = Duration.ofMinutes(5);

    public synchronized void add(EndpointMetricSnapshot snapshot) {
        window.addLast(snapshot);
        cleanup();
    }

    public synchronized List<EndpointMetricSnapshot> snapshots() {
        cleanup(); 
        return new ArrayList<>(window);
    }

    private void cleanup() {
        Instant cutoff = Instant.now().minus(WINDOW);
        while (!window.isEmpty()
            && window.peekFirst().getTimestamp().isBefore(cutoff)) {
            window.pollFirst();
        }
    }
}