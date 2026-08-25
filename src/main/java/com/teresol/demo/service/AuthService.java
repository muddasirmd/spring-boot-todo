package com.teresol.demo.service;

import org.springframework.stereotype.Service;

import com.teresol.demo.dto.request.AuthRequest;
import com.teresol.demo.dto.response.AuthResponse;
import com.teresol.demo.entity.User;
import com.teresol.demo.exception.DuplicateEmailException;
import com.teresol.demo.repository.UserRepository;

@Service
public class AuthService {

    public final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    public AuthResponse register(AuthRequest request){
        
        // Email Uniqueness
        if(userRepository.existsByEmail(request.email)){
            
            throw new DuplicateEmailException(request.email);
        }

        // Username Uniqueness

        User user = new User();
        user.setEmail(request.email);
        user.setUsername(request.username);
        user.setPassword(request.password);
        user.setEnabled("");
        user.setRole("USER");
        
        user = userRepository.save(user);
        
        return AuthResponse.builder()
            .id(user.getUserId())
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole())
            .build();

    }
}
