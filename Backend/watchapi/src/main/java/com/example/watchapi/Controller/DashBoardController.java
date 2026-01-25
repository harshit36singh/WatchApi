package com.example.watchapi.Controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watchapi.Service.ActuatorDash;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/watch")
@RequiredArgsConstructor
public class DashBoardController {
    
    private final ActuatorDash actuatorDash;


    @GetMapping("/dashboard")
    public Mono<Map<String,Object>> dashboard(@RequestParam String actuatorurl ){
        return actuatorDash.dashboard(actuatorurl);
    }
}
