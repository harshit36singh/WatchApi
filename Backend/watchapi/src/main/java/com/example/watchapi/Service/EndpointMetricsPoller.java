package com.example.watchapi.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.watchapi.Dto.EndpointMetricSnapshot;

import reactor.core.publisher.Flux;

@EnableScheduling
@Service
public class EndpointMetricsPoller {

    private static final Logger log = LoggerFactory.getLogger(EndpointMetricsPoller.class);
    
    private final EndpointDiscoveryService discovery;
    private final ActuatorService actuator;
    private final EndpointMetricsWindow store;

    private static final String ACTUATOR_URL = "http://localhost:8080/actuator";

    public EndpointMetricsPoller(
            EndpointDiscoveryService discovery,
            ActuatorService actuator,
            EndpointMetricsWindow store) {
        this.discovery = discovery;
        this.actuator = actuator;
        this.store = store;
    }

    @Scheduled(fixedRate = 30000)
    public void poll() {
        log.info("Polling metrics at {}", Instant.now());
        discovery.discover(ACTUATOR_URL)
            .flatMapMany(Flux::fromIterable)
            .flatMap(ep -> actuator.httpMetrics(
                    ACTUATOR_URL,
                    ep.getPath(),
                    ep.getMethod())
                .map(metric -> {
                    var snapshot = new EndpointMetricSnapshot(
                        Instant.now(),
                        extractController(ep.getHandler()),
                        ep.getMethod(),
                        ep.getPath(),
                        extractCount(metric)
                    );
                    log.debug("Adding snapshot: {}", snapshot);
                    return snapshot;
                }))
            .doOnNext(store::add)
            .doOnComplete(() -> log.info("Poll complete"))
            .doOnError(e -> log.error("Poll error", e))
            .subscribe();
    }

    private double extractCount(Map metric) {
        List<Map> m = (List<Map>) metric.get("measurements");
        return m.isEmpty() ? 0 : (double) m.get(0).get("value");
    }

    private String extractController(String handler) {
        return handler.substring(
                handler.lastIndexOf('.') + 1,
                handler.indexOf('#'));
    }

}