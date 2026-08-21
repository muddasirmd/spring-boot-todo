package com.teresol.demo.exception;

public record FieldErrorResponse (
    String field,
    String message
) {};
