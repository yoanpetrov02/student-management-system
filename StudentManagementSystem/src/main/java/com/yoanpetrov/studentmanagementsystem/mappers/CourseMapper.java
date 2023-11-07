package com.yoanpetrov.studentmanagementsystem.mappers;

import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.rest.dto.CourseCreationDto;
import com.yoanpetrov.studentmanagementsystem.rest.dto.CourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    Course convertDtoToEntity(CourseDto dto);

    Course convertCreationDtoToEntity(CourseCreationDto dto);
}
