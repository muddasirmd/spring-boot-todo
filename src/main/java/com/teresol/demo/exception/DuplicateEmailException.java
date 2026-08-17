package com.teresol.demo.exception;

public class DuplicateEmailException extends RuntimeException {
    
    public DuplicateEmailException(String email){
        super("Customer with email" + email + " already exists.");
    }
}
