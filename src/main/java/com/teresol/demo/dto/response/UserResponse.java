package com.teresol.demo.dto.response;

import com.teresol.demo.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    
    public long id;
    public String username;
    public String email;
    public Role role;
}
