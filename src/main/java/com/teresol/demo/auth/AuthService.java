package com.teresol.demo.auth;

import java.time.Instant;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.teresol.demo.auth.dto.request.LoginRequest;
import com.teresol.demo.auth.dto.request.RegisterRequest;
import com.teresol.demo.auth.dto.response.AuthResponse;
import com.teresol.demo.auth.dto.response.RegisterResponse;
import com.teresol.demo.entity.RefreshToken;
import com.teresol.demo.entity.User;
import com.teresol.demo.exception.DuplicateEmailException;
import com.teresol.demo.exception.DuplicateUsernameException;
import com.teresol.demo.repository.RefreshTokenRepository;
import com.teresol.demo.repository.UserRepository;
import com.teresol.demo.security.CustomUserDetails;
import com.teresol.demo.security.JwtService;
import com.teresol.demo.security.RefreshTokenService;
import com.teresol.demo.user.Role;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService,
        JwtService jwtService,
        RefreshTokenService refreshTokenService,
        RefreshTokenRepository refreshTokenRepository
        ){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
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

        // UserDetails userDetails = userDetailsService.loadUserByUsername(request.username);
        User user = userRepository.findByUsername(request.username).orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);
        // String refreshToken = jwtService.generateRefreshToken(userDetails);
        String refreshToken = refreshTokenService.create(user);

        return new AuthResponse(
            accessToken,
            refreshToken,
            "Bearer",
            900
        );
    }

    @Transactional
    public AuthResponse refresh(String refreshToken){

        RefreshToken oldToken = refreshTokenService.validate(refreshToken);

        User user = oldToken.getUser();

        oldToken.setRevokedAt(Instant.now());

        String newRefreshToken = refreshTokenService.create(user);

        // optionally set replacedBy here

        // UserDetails userDetails = new CustomUserDetails(user);
        

        String newAccessToken = jwtService.generateAccessToken(user);

        refreshTokenRepository.save(oldToken);


        // if(!jwtService.isRefreshTokenValid(refreshToken)){
        //     throw new BadCredentialsException("Invalid refresh token");
        // }

        // String username = jwtService.extractUsername(refreshToken);

        // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // String newAccessToken = jwtService.generateAccessToken(userDetails);

        return new AuthResponse(
            newAccessToken,
            newRefreshToken,
            "Bearer",
            900
        );
    }

    // Logout current device
    @Transactional 
    public void logout(String rawToken){

        RefreshToken refreshToken = refreshTokenService.validate(rawToken);

        refreshToken.setRevokedAt(Instant.now());

        refreshTokenRepository.save(refreshToken);
    }

    // Logout all devices
    @Transactional
    public void logoutAll(User user){

        List<RefreshToken> tokens = refreshTokenRepository.findByUserAndRevokedAtIsNull(user);

        Instant now = Instant.now();

        tokens.forEach(token -> token.setRevokedAt(now));

        refreshTokenRepository.saveAll(tokens);
    }
}
