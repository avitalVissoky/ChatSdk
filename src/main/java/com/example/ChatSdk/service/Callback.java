package com.example.ChatSdk.service;

public interface Callback<T> {
    void onSuccess(T result);
    void onFailure(Throwable throwable);
}
