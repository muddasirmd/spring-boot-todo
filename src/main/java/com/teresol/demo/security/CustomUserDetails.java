package com.teresol.demo.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.teresol.demo.entity.User;

public class CustomUserDetails implements UserDetails {
    
    private final User user;

    public  CustomUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorties = new HashSet<>();

        user.getRoles().forEach(role -> {

            authorties.add(
                new SimpleGrantedAuthority(
                    "ROLE_" + role.getName()
                )
            );

            role.getPermissions().forEach(permission -> {
                authorties.add(
                    new SimpleGrantedAuthority(permission.getName())
                );
            });
        });

        return  authorties;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }
}
