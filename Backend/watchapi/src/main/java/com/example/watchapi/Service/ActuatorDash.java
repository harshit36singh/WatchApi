package com.example.watchapi.Service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ActuatorDash {

    private final ActuatorService actuator;

    public Mono<Map<String, Object>> dashboard(String actuatorurl) {

        Mono<Map> health = actuator.health(actuatorurl);

        Mono<Map> cpu = safe(actuator.metric(actuatorurl, "system.cpu.count"));
        Mono<Map> uptime = safe(actuator.metric(actuatorurl, "process.uptime"));

        Mono<Map> totalReq = safe(actuator.metric(actuatorurl, "http.server.requests"));
        Mono<Map> r200 = safe(actuator.metricWithTag(actuatorurl, "http.server.requests", "status:200"));
        Mono<Map> r400 = safe(actuator.metricWithTag(actuatorurl, "http.server.requests", "status:400"));
        Mono<Map> r404 = safe(actuator.metricWithTag(actuatorurl, "http.server.requests", "status:404"));
        Mono<Map> r500 = safe(actuator.metricWithTag(actuatorurl, "http.server.requests", "status:500"));

        return Mono.zip(health, cpu, uptime, totalReq, r200, r400, r404, r500)
                .map(t -> Map.of(
                        "systemStatus", t.getT1().get("status"),
                        "diskFreeGB", extractDiskGB(t.getT1()),
                        "processors", extractValue(t.getT2()),
                        "uptimeSeconds", extractValue(t.getT3()),
                        "responses", Map.of(
                                "200", extractValue(t.getT5()),
                                "400", extractValue(t.getT6()),
                                "404", extractValue(t.getT7()),
                                "500", extractValue(t.getT8())
                        ),
                        "totalRequests", extractValue(t.getT4())
                ));
    }
    private Mono<Map> safe(Mono<Map> mono) {
        return mono.onErrorResume(e -> emptyMetric());
    }

    private Mono<Map> emptyMetric() {
        return Mono.just(
                Map.of(
                        "measurements",
                        List.of(Map.of("value", 0.0))
                )
        );
    }

    private double extractValue(Map metric) {
        List<Map> m = (List<Map>) metric.get("measurements");
        return ((Number) m.get(0).get("value")).doubleValue();
    }

    private double extractDiskGB(Map health) {
        Map components = (Map) health.get("components");
        Map disk = (Map) components.get("diskSpace");
        Map details = (Map) disk.get("details");
        return ((Number) details.get("free")).doubleValue() / 1_000_000_000;
    }
}
