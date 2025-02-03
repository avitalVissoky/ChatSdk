package com.example.ChatSdk.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List; // Place imports here
import java.util.ArrayList;

public class ChatRoom {
    private String id; // Firebase generates a document ID if not set
    private String name; // Group chat name (null for 1-on-1 chats)
    private List<String> participants; // List of User IDs
    private String lastMessageId; // ID of the last message
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")  // Specify the date format
    private Date createdAt; // Timestamp for chat room creation
    private boolean isGroupChat; // True for group chat, false for 1-on-1

    // Empty constructor for Firestore
    public ChatRoom() {
    }

    // Constructor
    public ChatRoom(String id, String name, List<String> participants, String lastMessageId, Date createdAt,
            boolean isGroupChat) {
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.lastMessageId = lastMessageId;
        this.createdAt = createdAt;
      //  this.isGroupChat = isGroupChat;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

//    public boolean isGroupChat() {
//        return isGroupChat;
//    }
//
//    public void setGroupChat(boolean groupChat) {
//        isGroupChat = groupChat;
//    }
}
