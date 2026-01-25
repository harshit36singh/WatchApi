package com.example.watchapi.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.watchapi.Dto.ApiEndpoint;
import com.example.watchapi.Dto.EndpointMetricSnapshot;
import com.example.watchapi.Dto.EndpointWithMetrics;
import com.example.watchapi.Dto.SimpleEndpoint;
import com.example.watchapi.Dto.UnusedEndpoint;
import com.example.watchapi.Dto.UsedEndpoint;
import com.example.watchapi.Service.EndpointDiscoveryService;
import com.example.watchapi.Service.EndpointMetricsWindow;
import com.example.watchapi.Service.TimeWindowUnusedService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/watche")
@RequiredArgsConstructor
public class EndpointController {

    private final EndpointDiscoveryService service;
    private final TimeWindowUnusedService timeWindowUnusedService;
    private final EndpointMetricsWindow endpointMetricsWindow;

    @GetMapping("/endpoints")
    public Mono<List<ApiEndpoint>> endpoints(
            @RequestParam String actuatorUrl) {

        return service.discover(actuatorUrl);
    }

    @GetMapping("/grouped")
    public Mono<Map<String, List<SimpleEndpoint>>> grouped(
            @RequestParam(defaultValue = "http://localhost:8080/actuator") String actuatorUrl) {

        return service.discoverGrouped(actuatorUrl);
    }

    @GetMapping("/metrics")
    public Mono<Map<String, List<EndpointWithMetrics>>> endpointsWithMetrics(
            @RequestParam(defaultValue = "http://localhost:8080/actuator") String actuatorUrl) {

        return service.discoverWithMetrics(actuatorUrl);
    }

    @GetMapping("/unused")
    public Mono<List<UnusedEndpoint>> unusedEndpoints(
            @RequestParam(defaultValue = "http://localhost:8080/actuator") String actuatorUrl) {

        return service.detectUnused(actuatorUrl);
    }

    @GetMapping("/lastmins")
    public List<UsedEndpoint> unusedLast5Min() {
        return timeWindowUnusedService.detect();
    }

    @GetMapping("/debug/snapshots")
   public List<EndpointMetricSnapshot> debugSnapshots() {
       return endpointMetricsWindow.snapshots();
   }
}
