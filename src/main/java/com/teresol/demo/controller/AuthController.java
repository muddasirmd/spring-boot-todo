package com.teresol.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teresol.demo.dto.request.AuthRequest;
import com.teresol.demo.dto.response.AuthResponse;
import com.teresol.demo.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("auth/register")
    public AuthResponse register(@Valid @RequestBody AuthRequest request){
        
        return authService.register(request);
    }
}
