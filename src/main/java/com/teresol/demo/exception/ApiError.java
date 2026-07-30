package com.teresol.demo.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError (
    LocalDateTime timestamp,
    int status,
    String message,
    String path,
    List<FieldError> errors
){};