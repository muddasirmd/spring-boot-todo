package com.teresol.demo.service;

import com.teresol.demo.dto.request.AuthRequest;
import com.teresol.demo.dto.response.AuthResponse;

public class AuthService {
    
    public AuthResponse register(AuthRequest request){
        
        return AuthResponse.builder()
            .id(1L)
            .username("Pasha")
            .email("pasha@mail.com")
            .role("USER")
            .build();

    }
}
