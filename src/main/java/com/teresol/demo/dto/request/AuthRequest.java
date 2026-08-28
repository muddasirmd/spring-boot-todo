package com.teresol.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 4, max = 8, message = "Password must be between 3 and 8 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    public String password;
}
