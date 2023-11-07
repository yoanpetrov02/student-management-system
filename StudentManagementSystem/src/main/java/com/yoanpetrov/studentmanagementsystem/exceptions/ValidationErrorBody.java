package com.yoanpetrov.studentmanagementsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ValidationErrorBody {

    private String status;
    private String errorMessage;
    private Map<String, String> errors;
}
