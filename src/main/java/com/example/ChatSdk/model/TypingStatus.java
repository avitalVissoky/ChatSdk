package com.example.ChatSdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypingStatus {
    @JsonProperty("isTyping")
    private boolean isTyping;

    public TypingStatus() {}

    public TypingStatus(boolean isTyping) {
        this.isTyping = isTyping;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean isTyping) {
        this.isTyping = isTyping;
    }

}
