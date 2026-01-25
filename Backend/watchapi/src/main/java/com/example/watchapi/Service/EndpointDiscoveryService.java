package com.example.watchapi.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.example.watchapi.Dto.ApiEndpoint;
import com.example.watchapi.Dto.EndpointWithMetrics;
import com.example.watchapi.Dto.SimpleEndpoint;
import com.example.watchapi.Dto.UnusedEndpoint;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EndpointDiscoveryService {

    private final ActuatorService actuator;
    private final EndpointMetricsService endpointMetricsService;

    

    public Mono<List<ApiEndpoint>> discover(String actuatorUrl) {

        return actuator.mappings(actuatorUrl)
                .map(this::extractEndpoints);
    }

   private List<ApiEndpoint> extractEndpoints(Map root) {

    List<ApiEndpoint> endpoints = new ArrayList<>();

    Map contexts = (Map) root.get("contexts");
    Map app = (Map) contexts.values().iterator().next();

    Map mappings = (Map) app.get("mappings");
    Map dispatcherServlets = (Map) mappings.get("dispatcherServlets");

    if (dispatcherServlets == null) {
        return endpoints;
    }

    List<Map> servletMappings =
            (List<Map>) dispatcherServlets.get("dispatcherServlet");

    for (Map entry : servletMappings) {

    String handler = (String) entry.get("handler");
    String predicate = (String) entry.get("predicate");

    if (handler == null || predicate == null) {
        continue;
    }

    if (!handler.contains("#")) {
        continue;
    }

    int methodEnd = predicate.indexOf(" ");
    if (methodEnd == -1) {
        continue;
    }

    String method = predicate.substring(1, methodEnd);

    int open = predicate.indexOf("[");
    int close = predicate.indexOf("]");

    if (open == -1 || close == -1 || close <= open) {
        continue;
    }

    String path = predicate.substring(open + 1, close);

    endpoints.add(new ApiEndpoint(method, path, handler));
}

    return endpoints;
}
public Mono<Map<String, List<SimpleEndpoint>>> discoverGrouped(
        String actuatorUrl) {

    return discover(actuatorUrl)
            .map(this::groupByController);
}
private Map<String, List<SimpleEndpoint>> groupByController(
        List<ApiEndpoint> endpoints) {

    Map<String, List<SimpleEndpoint>> grouped = new LinkedHashMap<>();

    for (ApiEndpoint ep : endpoints) {

        String handler = ep.getHandler();

        String controller =
                handler.substring(
                        handler.lastIndexOf(".") + 1,
                        handler.indexOf("#")
                );

        grouped
            .computeIfAbsent(controller, k -> new ArrayList<>())
            .add(new SimpleEndpoint(ep.getMethod(), ep.getPath()));
    }

    return grouped;
}

public Mono<Map<String, List<EndpointWithMetrics>>> discoverWithMetrics(
        String actuatorUrl) {

    return discover(actuatorUrl)
        .flatMapMany(Flux::fromIterable)
        .flatMap(ep ->
            endpointMetricsService.enrich(actuatorUrl, ep)
                .map(enriched -> Map.entry(ep, enriched))
        )
        .collectList()
        .map(this::groupEnriched);
}
private Map<String, List<EndpointWithMetrics>> groupEnriched(
        List<Map.Entry<ApiEndpoint, EndpointWithMetrics>> list) {

    Map<String, List<EndpointWithMetrics>> grouped = new LinkedHashMap<>();

    for (var entry : list) {

        String handler = entry.getKey().getHandler();
        String controller =
            handler.substring(
                handler.lastIndexOf(".") + 1,
                handler.indexOf("#")
            );

        grouped
            .computeIfAbsent(controller, k -> new ArrayList<>())
            .add(entry.getValue());
    }

    return grouped;
}

public Mono<List<UnusedEndpoint>> detectUnused(String actuatorUrl) {

    return discoverWithMetrics(actuatorUrl)
        .map(this::findUnused);
}
private List<UnusedEndpoint> findUnused(
    Map<String, List<EndpointWithMetrics>> grouped) {

    List<UnusedEndpoint> unused = new ArrayList<>();

    for (var entry : grouped.entrySet()) {

        String controller = entry.getKey();

        for (EndpointWithMetrics ep : entry.getValue()) {

            Object totalObj = ep.getMetrics().get("total");
            double total = totalObj == null ? 0 : (double) totalObj;

            if (total == 0) {
                unused.add(new UnusedEndpoint(
                    controller,
                    ep.getMethod(),
                    ep.getPath()
                ));
            }
        }
    }

    return unused;
}


}
