package com.teresol.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.teresol.demo.dto.request.AuthRequest;
import com.teresol.demo.dto.response.AuthResponse;
import com.teresol.demo.entity.User;
import com.teresol.demo.exception.DuplicateEmailException;
import com.teresol.demo.exception.DuplicateUsernameException;
import com.teresol.demo.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public AuthResponse register(AuthRequest request){
        
        if(userRepository.existsByEmail(request.email)){
            throw new DuplicateEmailException(request.email);
        }

        if(userRepository.existsByUsername(request.username)){
            throw new DuplicateUsernameException(request.username);
        }

        User user = new User();
        user.setEmail(request.email);
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setEnabled("Y");
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
