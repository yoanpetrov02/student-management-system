package com.yoanpetrov.studentmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ValidationErrorResponse {

    private String status;
    private String errorMessage;
    private Map<String, String> errors;
}
