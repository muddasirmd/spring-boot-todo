package com.teresol.demo.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.teresol.demo.auth.dto.request.LoginRequest;
import com.teresol.demo.auth.dto.request.RegisterRequest;
import com.teresol.demo.auth.dto.response.AuthResponse;
import com.teresol.demo.auth.dto.response.RegisterResponse;
import com.teresol.demo.entity.User;
import com.teresol.demo.exception.DuplicateEmailException;
import com.teresol.demo.exception.DuplicateUsernameException;
import com.teresol.demo.repository.UserRepository;
import com.teresol.demo.security.JwtService;
import com.teresol.demo.user.Role;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService,
        JwtService jwtService){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }
    
    public void register(RegisterRequest request){
        
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
        user.setEnabled(true);
        user.setRole(Role.USER);
        
        user = userRepository.save(user);
        
        // return RegisterResponse.builder()
        //     .id(user.getUserId())
        //     .username(user.getUsername())
        //     .email(user.getEmail())
        //     .role(user.getRole().name())
        //     .build();
    }

    public AuthResponse login(LoginRequest request){

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username, request.password)
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(
            accessToken,
            refreshToken,
            "Bearer",
            900
        );
    }
}
