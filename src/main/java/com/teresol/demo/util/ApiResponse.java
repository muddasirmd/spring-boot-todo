package com.teresol.demo.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Boolean isError;
    private String  message;
    private T       data;
    private List<String> errorDetails;


    // ─────────────────────────────────────────────────────
    // Success factories
    // ─────────────────────────────────────────────────────

    /** Success with data only */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .isError(false)
                .data(data)
                .build();
    }

    /** Success with message only — no data (e.g. delete, approve) */
    public static <T> ApiResponse<T> successMessage(String message) {
        return ApiResponse.<T>builder()
                .isError(false)
                .message(message)
                .build();
    }

    /** Success with data and message */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .isError(false)
                .data(data)
                .message(message)
                .build();
    }

    /** Success with no data and no message (rare) */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .isError(false)
                .build();
    }

    // ─────────────────────────────────────────────────────
    // Error factories
    // ─────────────────────────────────────────────────────

    /** Error with message only */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .isError(true)
                .message(message)
                .build();
    }

    /** Error with message and error code */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .isError(true)
                .message(message)
                .build();
    }
}
