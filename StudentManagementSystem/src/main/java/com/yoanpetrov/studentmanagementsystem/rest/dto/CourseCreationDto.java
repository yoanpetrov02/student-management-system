package com.yoanpetrov.studentmanagementsystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CourseCreationDto {
    private String name;
    private String description;
    private int maxCapacity;
}
