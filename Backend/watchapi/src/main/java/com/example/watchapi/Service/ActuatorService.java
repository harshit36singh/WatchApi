package com.example.watchapi.Service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ActuatorService {
    private final WebClient client = WebClient.create();

    public Mono<Map> health(String actuator) {
        return client.get().uri(actuator + "/health").retrieve().bodyToMono(Map.class);
    }
}
