package com.example.ChatSdk.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore firestore() {
        try {
            String serviceAccountPath = System.getenv("FIREBASE_CONFIG_PATH");

            if (serviceAccountPath == null || serviceAccountPath.isEmpty()) {
                throw new RuntimeException("Environment variable FIREBASE_CONFIG_PATH is not set or is empty.");
            }

            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountPath));
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            return FirestoreClient.getFirestore();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing Firestore: " + e.getMessage(), e);
        }
    }
}
