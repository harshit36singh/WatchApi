package com.example.watchapi.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.watchapi.Dto.EndpointMetricSnapshot;
import com.example.watchapi.Dto.UnusedEndpoint;
import com.example.watchapi.Dto.UsedEndpoint;

@Service
public class TimeWindowUnusedService {

    private final EndpointMetricsWindow window;

    public TimeWindowUnusedService(EndpointMetricsWindow window) {
        this.window = window;
    }

    public List<UsedEndpoint> detect() {

    Map<String, List<EndpointMetricSnapshot>> grouped =
        window.snapshots().stream()
            .collect(Collectors.groupingBy(
                s -> s.getController() + "|" + s.getMethod() + "|" + s.getPath()
            ));

    List<UsedEndpoint> used = new ArrayList<>();

    for (var entry : grouped.entrySet()) {

        List<EndpointMetricSnapshot> list = entry.getValue();

        if (list.size() < 2) continue;

        list.sort(Comparator.comparing(EndpointMetricSnapshot::getTimestamp));

        EndpointMetricSnapshot first = list.get(0);
        EndpointMetricSnapshot last = list.get(list.size() - 1);

        long delta = (long) (last.getTotalRequests() - first.getTotalRequests());

        used.add(new UsedEndpoint(
            last.getController(),
            last.getMethod(),
            last.getPath(),
            delta  
        ));
    }

    return used;
}
}
