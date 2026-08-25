package com.teresol.demo.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    
    @NotBlank
    public String username;
    
    @NotBlank
    @Email
    public String email;
    
    @NotBlank
    public String password;
}
