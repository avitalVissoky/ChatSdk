package com.example.ChatSdk.controller;

import com.example.ChatSdk.model.ChatRoom;
import com.example.ChatSdk.model.Message;
import com.example.ChatSdk.model.TypingStatus;
import com.example.ChatSdk.service.Callback;
import com.example.ChatSdk.service.FirestoreService;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final FirestoreService firestoreService;

    @Autowired
    public ChatController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createChatRoom(@RequestBody ChatRoom chatRoom) {
        firestoreService.addChatRoom(chatRoom)
                .addListener(() -> ResponseEntity.ok("Chat room created successfully!"),
                        ex -> ResponseEntity.internalServerError().body("Error: " + ex.toString()));
        return ResponseEntity.ok("Creating chat room...");
    }

    @GetMapping("/rooms/{userId}")
    public CompletableFuture<ResponseEntity<List<ChatRoom>>> getUserChatRooms(@PathVariable String userId) {
        CompletableFuture<ResponseEntity<List<ChatRoom>>> futureResponse = new CompletableFuture<>();

        firestoreService.getUserChatRooms(userId, new Callback<List<ChatRoom>>() {
            @Override
            public void onSuccess(List<ChatRoom> result) {
                futureResponse.complete(ResponseEntity.ok(result));
            }

            @Override
            public void onFailure(Throwable throwable) {
                futureResponse.complete(ResponseEntity.internalServerError().body(null));
            }
        });

        return futureResponse;
    }

    @PostMapping("/message/{chatRoomId}")
    public ResponseEntity<String> sendMessage(@PathVariable String chatRoomId, @RequestBody Message message) {
        message.setChatRoomId(chatRoomId);
        firestoreService.addMessage(message, new Callback<WriteResult>() {
            @Override
            public void onSuccess(WriteResult result) {
                ResponseEntity.ok("Message sent successfully!");
            }

            @Override
            public void onFailure(Throwable throwable) {
                ResponseEntity.badRequest().body("Error: " + throwable.getMessage());
            }
        });
        return ResponseEntity.ok("Sending message...");
    }

    @PostMapping("message/delete/{msgId}")
    public ResponseEntity<String> deleteMessage(@PathVariable String msgId) {
        firestoreService.deleteMessageAsync(msgId, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                ResponseEntity.ok("Message deleted successfully");
            }

            @Override
            public void onFailure(Throwable throwable) {
                ResponseEntity.badRequest().body("Error: " + throwable.getMessage());
            }
        });
        return ResponseEntity.ok("Deleting message...");
    }

    @GetMapping("/messages/{chatRoomId}")
    public CompletableFuture<ResponseEntity<?>> getMessages(
            @PathVariable String chatRoomId,
            @RequestParam(required = false) String lastMessageId,
            @RequestParam(defaultValue = "20") int pageSize) {

        CompletableFuture<ResponseEntity<?>> futureResponse = new CompletableFuture<>();

        firestoreService.getMessages(chatRoomId, lastMessageId, pageSize, new Callback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> result) {
                futureResponse.complete(ResponseEntity.ok(result));
            }

            @Override
            public void onFailure(Throwable throwable) {
                futureResponse.complete(ResponseEntity.internalServerError().body("Error fetching messages: " + throwable.getMessage()));
            }
        });

        return futureResponse;
    }


    @PostMapping("/messages/typing/{chatRoomId}/{userId}")
    public ResponseEntity<String> setTypingIndicator(@PathVariable String chatRoomId, @PathVariable String userId, @RequestBody TypingStatus typingStatus) {
        firestoreService.updateTypingStatus(chatRoomId, userId, typingStatus, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                ResponseEntity.ok("Typing status updated successfully");
            }

            @Override
            public void onFailure(Throwable throwable) {
                ResponseEntity.internalServerError().body("Error updating typing status: " + throwable.getMessage());
            }
        });
        return ResponseEntity.ok("Updating typing status...");
    }
}
