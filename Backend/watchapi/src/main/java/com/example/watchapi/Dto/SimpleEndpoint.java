package com.example.watchapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleEndpoint {
    private String method;
    private String path;
}
