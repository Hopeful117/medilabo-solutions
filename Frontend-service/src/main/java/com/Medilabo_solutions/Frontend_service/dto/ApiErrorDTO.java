package com.Medilabo_solutions.Frontend_service.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ApiErrorDTO {
    private int status;
    private String error;
    private Map<String,String>errors;
}
