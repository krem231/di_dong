package com.example.manga;

public interface MangaCallback<T> {
    void onSuccess(T result);
    void onError(String error);
}
