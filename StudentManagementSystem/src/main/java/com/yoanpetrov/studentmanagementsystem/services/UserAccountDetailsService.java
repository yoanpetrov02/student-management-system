package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserAccountDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException
    {
        return repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
