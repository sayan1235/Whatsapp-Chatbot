package com.whatsapp.chatbot.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.json}")
    private String firebaseCredentials;

    @Value("${firebase.project.id}")
    private String projectId;

    @Bean
    public Firestore firestore() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(firebaseCredentials.getBytes()));

        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();

        return firestoreOptions.getService();
    }
}