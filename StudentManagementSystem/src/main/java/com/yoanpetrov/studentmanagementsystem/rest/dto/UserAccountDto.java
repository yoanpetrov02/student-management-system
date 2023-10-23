package com.yoanpetrov.studentmanagementsystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAccountDto {

    private String username;
    private String password;
}
