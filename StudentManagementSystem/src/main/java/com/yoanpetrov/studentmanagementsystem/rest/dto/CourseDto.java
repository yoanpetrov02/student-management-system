package com.yoanpetrov.studentmanagementsystem.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CourseDto {
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @Min(10)
    @Max(120)
    private int maxCapacity;
}
