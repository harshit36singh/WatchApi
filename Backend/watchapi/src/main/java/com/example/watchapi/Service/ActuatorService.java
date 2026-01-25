package com.example.watchapi.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

@Service
public class ActuatorService {

    private final WebClient client = WebClient.create();

    public Mono<Map> health(String actuator) {
        return client.get()
                .uri(actuator + "/health")
                .retrieve()
                .bodyToMono(Map.class);
    }

   public Mono<Map> metric(String url, String metric) {
        return client.get()
                .uri(url + "/metrics/" + metric)
                .retrieve()
                .bodyToMono(Map.class);
    }

    public Mono<Map> metricWithTag(String url, String metric, String tag) {
        return client.get()
                .uri(url + "/metrics/" + metric + "?tag=" + tag)
                .retrieve()
                .bodyToMono(Map.class);
    }

    private Mono<Map> emptyMetric() {
        return Mono.just(
            Map.of(
                "measurements",
                java.util.List.of(
                    Map.of("value", 0.0)
                )
            )
        );
    }

    public Mono<Map> mappings(String actuatorUrl) {
        return client.get()
                .uri(actuatorUrl + "/mappings")
                .retrieve()
                .bodyToMono(Map.class);
    }

 public Mono<Map> httpMetrics(String actuatorUrl, String uri, String method) {
    
    return WebClient.create()
        .get()
        .uri(actuatorUrl +
             "/metrics/http.server.requests" +
             "?tag=method:" + method)  
        .retrieve()
        .bodyToMono(Map.class)
        .onErrorReturn(Map.of("measurements", List.of()));
}

    
}
