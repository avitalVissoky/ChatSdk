package com.example.ChatSdk.service;

import com.example.ChatSdk.model.ChatRoom;
import com.example.ChatSdk.model.Message;
import com.example.ChatSdk.model.TypingStatus;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private final Firestore db;

    public FirestoreService(Firestore db) {
        this.db = db;
    }

    public ApiFuture<WriteResult> addChatRoom(ChatRoom chatRoom) {
        DocumentReference docRef = db.collection("chatRooms").document(chatRoom.getId());
        return docRef.set(chatRoom);
    }

    public void getUserChatRooms(String userId, Callback<List<ChatRoom>> callback) {
        CollectionReference chatRoomsRef = db.collection("chatRooms");
        Query query = chatRoomsRef.whereArrayContains("participants", userId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        querySnapshot.addListener(() -> {
            try {
                List<ChatRoom> chatRooms = new ArrayList<>();
                for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
                    chatRooms.add(document.toObject(ChatRoom.class));
                }
                callback.onSuccess(chatRooms);
            } catch (ExecutionException | InterruptedException e) {
                callback.onFailure(e);
            }
        }, MoreExecutors.directExecutor());
    }

    public void addMessage(Message message, Callback<WriteResult> callback) {
        DocumentReference docRef = db.collection("messages").document(message.getId());
        ApiFuture<DocumentSnapshot> getFuture = docRef.get();

        getFuture.addListener(() -> {
            try {
                DocumentSnapshot snapshot = getFuture.get();
                if (snapshot.exists()) {
                    callback.onFailure(new IllegalArgumentException("Message with this ID already exists"));
                } else {
                    ApiFuture<WriteResult> setFuture = docRef.set(message);
                    setFuture.addListener(() -> {
                        try {
                            WriteResult result = setFuture.get();
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure(new RuntimeException("Error saving message", e));
                        }
                    }, MoreExecutors.directExecutor());
                }
            } catch (Exception e) {
                callback.onFailure(new RuntimeException("Error checking message existence", e));
            }
        }, MoreExecutors.directExecutor());
    }

    public void getMessages(String chatRoomId, String lastMessageId, int pageSize, Callback<List<Message>> callback) {
        CollectionReference messagesRef = db.collection("messages");
        Query query = messagesRef
                .whereEqualTo("chatRoomId", chatRoomId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(pageSize);

        if (lastMessageId != null && !lastMessageId.isEmpty()) {
            messagesRef.document(lastMessageId).get().addListener(() -> {
                try {
                    DocumentSnapshot lastMessageSnapshot = messagesRef.document(lastMessageId).get().get();
                    Query finalQuery = query;
                    if (lastMessageSnapshot.exists()) {
                        finalQuery = finalQuery.startAfter(lastMessageSnapshot);
                    }
                    executeQuery(finalQuery, callback);
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }, MoreExecutors.directExecutor());
        } else {
            executeQuery(query, callback);
        }
    }


    private void executeQuery(Query query, Callback<List<Message>> callback) {
        ApiFuture<QuerySnapshot> future = query.get();

        future.addListener(() -> {
            try {
                List<Message> messages = new ArrayList<>();
                for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
                    messages.add(doc.toObject(Message.class));
                }
                callback.onSuccess(messages);
            } catch (ExecutionException | InterruptedException e) {
                callback.onFailure(e);
            }
        }, MoreExecutors.directExecutor());
    }

    public void updateTypingStatus(String chatRoomId, String userId, TypingStatus typingStatus, Callback<Void> callback) {
        DocumentReference docRef = db.collection("chatRooms").document(chatRoomId);
        ApiFuture<WriteResult> result = docRef.update("typingStatus." + userId, typingStatus.isTyping());

        result.addListener(() -> {
            try {
                result.get();
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onFailure(e);
            }
        }, MoreExecutors.directExecutor());
    }

    public void deleteMessageAsync(String msgId, Callback<Void> callback) {
        DocumentReference docRef = db.collection("messages").document(msgId);

        ApiFuture<DocumentSnapshot> getFuture = docRef.get();

        getFuture.addListener(() -> {
            try {
                DocumentSnapshot snapshot = getFuture.get();
                if (!snapshot.exists()) {
                    callback.onFailure(new IllegalArgumentException("Message not found"));
                } else {
                    ApiFuture<WriteResult> deleteFuture = docRef.delete();
                    deleteFuture.addListener(() -> {
                        try {
                            deleteFuture.get();
                            callback.onSuccess(null);
                        } catch (Exception e) {
                            callback.onFailure(e);
                        }
                    }, MoreExecutors.directExecutor());
                }
            } catch (Exception e) {
                callback.onFailure(e);
            }
        }, MoreExecutors.directExecutor());
    }
}
