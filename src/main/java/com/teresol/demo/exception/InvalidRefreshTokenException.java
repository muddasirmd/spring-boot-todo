package com.teresol.demo.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    
    public InvalidRefreshTokenException(){
        super("Refresh Token Expired");
    }
}
