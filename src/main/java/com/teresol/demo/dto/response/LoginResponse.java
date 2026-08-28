package com.teresol.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    
    public Long id;
    public String accessToken;
    public String refreshToken;
    public String tokenType;
    public Integer expiresIn;
}