package com.teresol.demo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.teresol.demo.security.JwtProperties;

@Configuration 
@EnableConfigurationProperties (JwtProperties.class)
public class JwtConfig {
    
}
