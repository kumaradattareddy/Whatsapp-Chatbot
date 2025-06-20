package com.chatbot.demo.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

// Sets up Firebase for the application.

@Configuration
public class FirebaseAdminConfig {
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FirebaseOptions options;

        String jsonEnv = System.getenv("FIREBASE_CREDENTIALS_JSON");
        if (jsonEnv != null && !jsonEnv.isBlank()) {

            // 1) Load credentials from the environment variable

            ByteArrayInputStream creds = new ByteArrayInputStream(jsonEnv.getBytes(StandardCharsets.UTF_8));
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(creds))
                    .build();
        } else {

            // 2) Fallback to classpath (local dev)
            // Make sure to have firebase-service-account.json in src/main/resources

            ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();
        }

        // 3) Initialize app once
        // If already initialized, return the existing instance
        // This prevents re-initialization errors in Spring Boot
        
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}
