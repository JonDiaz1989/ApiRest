package com.apirest.users.common.api;

import java.time.Instant;

public final class ApiResponse<T> {
    private final T data;
    private final String message;
    private final Instant timestamp;

    private ApiResponse(T data, String message, Instant timestamp) {
        this.data = data;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, null, Instant.now());
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
