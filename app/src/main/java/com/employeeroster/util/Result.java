package com.employeeroster.util;

public class Result<T> {
    private final T data;
    private final String errorMessage;

    private Result(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null);
    }

    public static <T> Result<T> error(String errorMessage) {
        return new Result<>(null, errorMessage);
    }

    public boolean isSuccess() {
        return errorMessage == null;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
