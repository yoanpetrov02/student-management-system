package com.yoanpetrov.studentmanagementsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorBody {

    private String status;
    private String errorMessage;
}
