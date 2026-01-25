package com.example.watchapi.Service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.watchapi.Dto.ApiEndpoint;
import com.example.watchapi.Dto.EndpointWithMetrics;

import reactor.core.publisher.Mono;

@Service
public class EndpointMetricsService {

    private final ActuatorService actuator;

    public EndpointMetricsService(ActuatorService actuator) {
        this.actuator = actuator;
    }

    public Mono<EndpointWithMetrics> enrich(
            String actuatorUrl,
            ApiEndpoint ep) {

        Mono<Map> total =
            actuator.httpMetrics(actuatorUrl, ep.getPath(), ep.getMethod());

        Mono<Map> s2xx =
            actuator.httpMetrics(actuatorUrl, ep.getPath(), ep.getMethod())
                .flatMap(m -> actuator.httpMetrics(
                        actuatorUrl,
                        ep.getPath(),
                        ep.getMethod() + "&tag=status:2xx"
                ));

        Mono<Map> s4xx =
            actuator.httpMetrics(actuatorUrl, ep.getPath(), ep.getMethod())
                .flatMap(m -> actuator.httpMetrics(
                        actuatorUrl,
                        ep.getPath(),
                        ep.getMethod() + "&tag=status:4xx"
                ));

        Mono<Map> s5xx =
            actuator.httpMetrics(actuatorUrl, ep.getPath(), ep.getMethod())
                .flatMap(m -> actuator.httpMetrics(
                        actuatorUrl,
                        ep.getPath(),
                        ep.getMethod() + "&tag=status:5xx"
                ));

        return Mono.zip(total, s2xx, s4xx, s5xx)
            .map(t -> new EndpointWithMetrics(
                ep.getMethod(),
                ep.getPath(),
                Map.of(
                    "total", extractCount(t.getT1()),
                    "2xx", extractCount(t.getT2()),
                    "4xx", extractCount(t.getT3()),
                    "5xx", extractCount(t.getT4()),
                    "avgResponseMs", extractAvgMs(t.getT1())
                )
            ));
    }
    private double extractCount(Map metric) {
    List<Map> m = (List<Map>) metric.get("measurements");
    if (m.isEmpty()) return 0;
    return (double) m.get(0).get("value");
}

private double extractAvgMs(Map metric) {
    List<Map> m = (List<Map>) metric.get("measurements");
    if (m.size() < 2) return 0;

    double count = (double) m.get(0).get("value");
    double totalTime = (double) m.get(1).get("value");

    return count == 0 ? 0 : (totalTime / count) * 1000;
}

}
