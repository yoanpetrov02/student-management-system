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
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Description cannot be null")
    private String description;
    @Min(value = 10, message = "Max capacity cannot be below 10")
    @Max(value = 120, message = "Max capacity cannot be above 120")
    private int maxCapacity;
}
