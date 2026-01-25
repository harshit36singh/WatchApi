package com.example.watchapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnusedEndpoint {
    private String controller;
    private String method;
    private String path;
}

