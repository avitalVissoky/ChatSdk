package com.example.ChatSdk.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore firestore() {
        try {
            String firebaseConfigJson = System.getenv("FIREBASE_CONFIG_JSON");

            if (firebaseConfigJson == null || firebaseConfigJson.isEmpty()) {
                throw new RuntimeException("Environment variable FIREBASE_CONFIG_JSON is not set or is empty.");
            }

            InputStream serviceAccount = new ByteArrayInputStream(firebaseConfigJson.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
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
