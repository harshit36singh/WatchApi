package com.example.watchapi.Service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.watchapi.Dto.HttpTraceEntry;

@Component
public class HttpTraceStore {

    private static final int MAX = 100;
    private final Deque<HttpTraceEntry> traces = new LinkedList<>();

    public synchronized void add(HttpTraceEntry entry) {
        traces.addFirst(entry);
        if (traces.size() > MAX) {
            traces.removeLast();
        }
    }

    public synchronized List<HttpTraceEntry> getAll() {
        return new ArrayList<>(traces);
    }
}
