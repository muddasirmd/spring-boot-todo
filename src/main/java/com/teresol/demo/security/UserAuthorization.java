package com.teresol.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.teresol.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class UserAuthorization {
    
    private final UserRepository userRepository;
    
    public boolean canAccess(Long userId, Authentication authentication){

        boolean isAdmin = authentication
            .getAuthorities()
            .stream()
            .anyMatch(authority -> authority
                .getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }

        return userRepository
            .findById(userId)
            .map(user -> user.getUsername().equals(authentication.getName()))
            .orElse(false);
    }
    
}
