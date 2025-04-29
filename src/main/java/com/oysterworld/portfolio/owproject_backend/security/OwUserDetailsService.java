package com.oysterworld.portfolio.owproject_backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class OwUserDetailsService implements UserDetailsService {
    @Value("${app.admin.email}")
    private String adminEmail;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = User.builder()
                .username(username)
                .password("{noop}dummy")
                .roles(username.equals(adminEmail) ? "ADMIN" : "USER")
                .build();
        return user;
    }
}
