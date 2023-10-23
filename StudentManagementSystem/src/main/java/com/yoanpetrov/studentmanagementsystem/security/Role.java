package com.yoanpetrov.studentmanagementsystem.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
    // TODO: 19-Oct-23 Figure out the roles
    USER(Collections.emptySet()),
    TEACHER(Collections.emptySet()),
    ADMIN(Collections.emptySet())

    ;

    private final Set<Permission> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}
