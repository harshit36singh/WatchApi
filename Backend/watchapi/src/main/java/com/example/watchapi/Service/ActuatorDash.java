package com.example.watchapi.Service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ActuatorDash {
    
    private final ActuatorService actuatorService;

    public Mono<Map<String,Object>> dashboard(String actuatorurl){
    
        Mono<Map> health=actuatorService.health(actuatorurl);
        Mono<Map> total=actuatorService.requests(actuatorurl);
        Mono<Map> ok=actuatorService.requestByStatus(actuatorurl,200);
        Mono<Map> error=actuatorService.requestByStatus(actuatorurl,500);
        return Mono.zip(health,total,ok,error).map(c->Map.of(
            "status",c.getT1().get("status"),
            "totalreq",extractCount(c.getT2()),
            "success200",extractCount(c.getT3()),
            "error",extractCount(c.getT4())
        ));
    }

    private double extractCount(Map c){
        var val=(List<Map>)c.get("measurements");
        return (double)val.get(0).get("value");
    }
}
