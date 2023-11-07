package com.yoanpetrov.studentmanagementsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorBody {

    private String status;
    private String errorMessage;
}
