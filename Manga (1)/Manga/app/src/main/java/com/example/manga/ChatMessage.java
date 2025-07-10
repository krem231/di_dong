package com.example.manga;

public class ChatMessage {
    String message;
    boolean isUser;

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }
}
