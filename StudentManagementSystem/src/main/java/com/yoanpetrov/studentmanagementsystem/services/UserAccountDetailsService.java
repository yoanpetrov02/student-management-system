package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User account details service. Used to load a user account by its username from the database.
 */
@RequiredArgsConstructor
@Service
public class UserAccountDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;

    /**
     * Loads the user from the database.
     *
     * @param username the username identifying the user whose data is required.
     * @return the {@code UserDetails} object if the user exists.
     * @throws UsernameNotFoundException if a user with that username was not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException
    {
        return repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}