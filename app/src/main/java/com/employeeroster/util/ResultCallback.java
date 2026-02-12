package com.employeeroster.util;

public interface ResultCallback<T> {
    void onComplete(Result<T> result);
}
