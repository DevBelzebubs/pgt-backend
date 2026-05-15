package com.portable.shared.infrastructure.presentation;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        LocalDateTime timestamp) {

    public static <T> ApiResponse<T> ok(String message, T data) {
         return ApiResponse.<T>builder()
         .success(true)
         .message(message)
         .data(data)
         .timestamp(LocalDateTime.now())
         .build();
    }

    public static <T> ApiResponse<T> error(String message, T data) {
         return ApiResponse.<T>builder()
         .success(false)
         .message(message)
         .timestamp(LocalDateTime.now())
         .build();
    }

}
