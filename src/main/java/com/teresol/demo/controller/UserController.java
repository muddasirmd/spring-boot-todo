package com.teresol.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teresol.demo.dto.response.UserResponse;
import com.teresol.demo.entity.User;
import com.teresol.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    public final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication){

        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow();
        // return ResponseEntity.ok(authentication.getPrincipal());

        return ResponseEntity.ok(
            new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
            )
        );
    }
}
