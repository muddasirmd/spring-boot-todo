package com.teresol.demo.exception;

public record FieldError(
    String field,
    String message
) {};
