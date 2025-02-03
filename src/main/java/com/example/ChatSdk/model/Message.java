package com.example.ChatSdk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date; // Use Date instead of LocalDateTime

public class Message {
    private String id;
    private String chatRoomId;
    private String senderId;
    private String content;
    private String type;
    private Date timestamp; // Use Date instead of LocalDateTime
//    private boolean isRead;

    // Empty constructor for Firestore
    public Message() {}

    // Constructor
    public Message(String id, String chatRoomId, String senderId, String content, String type, Date timestamp) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
//        this.isRead = isRead;
    }

    // Getters and setters
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getChatRoomId() { return chatRoomId; }

    public void setChatRoomId(String chatRoomId) { this.chatRoomId = chatRoomId; }

    public String getSenderId() { return senderId; }

    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public Date getTimestamp() { return timestamp; }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

//    public boolean isRead() { return isRead; }

//    public void setRead(boolean isRead) { this.isRead = isRead; }
}
