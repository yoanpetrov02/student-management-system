package com.yoanpetrov.studentmanagementsystem.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An enum containing all available permissions of users.
 */
@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    TEACHER_CREATE("teacher:create"),
    TEACHER_READ("teacher:read"),
    TEACHER_UPDATE("teacher:update"),
    TEACHER_DELETE("teacher:delete"),

    ;

    private final String permission;
}
