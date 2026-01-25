package com.example.watchapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiEndpoint {
    private String method;
    private String path;
    private String handler;
}
