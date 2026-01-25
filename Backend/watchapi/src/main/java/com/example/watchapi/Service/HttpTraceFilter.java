package com.example.watchapi.Service;

import java.io.IOException;
import java.time.Instant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.watchapi.Dto.HttpTraceEntry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpTraceFilter extends OncePerRequestFilter {

    private final HttpTraceStore store;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;

            store.add(new HttpTraceEntry(
                Instant.now(),
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration
            ));
        }
    }
}
