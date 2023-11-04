package com.yoanpetrov.studentmanagementsystem.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import static com.yoanpetrov.studentmanagementsystem.security.Permission.*;

/**
 * An enum containing all available roles of users.
 * Each role has a set of {@code Permissions}.
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN(
      Set.of(
          ADMIN_CREATE,
          ADMIN_READ,
          ADMIN_UPDATE,
          ADMIN_DELETE,
          TEACHER_CREATE,
          TEACHER_READ,
          TEACHER_UPDATE,
          TEACHER_DELETE
      )),

    TEACHER(
        Set.of(
            TEACHER_CREATE,
            TEACHER_READ,
            TEACHER_UPDATE,
            TEACHER_DELETE
        )),

    STUDENT(Collections.emptySet())

    ;

    private final Set<Permission> permissions;

    /**
     * Returns the role's authorities as a list of {@code SimpleGrantedAuthority} instances.
     *
     * @return the list of authorities.
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}
