package com.teresol.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.teresol.demo.entity.User;
import com.teresol.demo.repository.UserRepository;
import com.teresol.demo.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(user);
        // return org.springframework.security.core.userdetails.User
        //         .withUsername(user.getUsername())
        //         .password(user.getPassword())
        //         .roles(user.getRole().name())
        //         .disabled(!user.isEnabled())
        //         .build();
    }
}