package com.yoanpetrov.studentmanagementsystem.mappers;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User convertDtoToEntity(UserDto dto);
}
