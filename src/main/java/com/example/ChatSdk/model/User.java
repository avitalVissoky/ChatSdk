package com.example.ChatSdk.model;

public class User {
    private String id; // Firebase automatically generates a document ID if not set
    private String username; // Display name
    private String email; // Optional: For account management
    private String profileImage; // URL of the profile picture
    private long lastSeen; // Timestamp for last online time
    private boolean isOnline; // Online status

    // Empty constructor for Firestore
    public User() {
    }

    // Constructor
    public User(String id, String username, String email, String profileImage, long lastSeen, boolean isOnline) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.lastSeen = lastSeen;
        this.isOnline = isOnline;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
