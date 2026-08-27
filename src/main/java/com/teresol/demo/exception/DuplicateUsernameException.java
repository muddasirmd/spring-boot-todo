package com.teresol.demo.exception;

public class DuplicateUsernameException extends RuntimeException {
    
    public DuplicateUsernameException(String name){
        super("User with username " + name + " already exists.");
    }
}
