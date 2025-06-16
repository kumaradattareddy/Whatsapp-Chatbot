package com.chatbot.demo.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseAdminConfig {

    /**
     * Initializes the FirebaseApp using the service-account JSON
     * placed in src/main/resources/firebase-service-account.json.
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // 1) Load the JSON file from the classpath
        ClassPathResource resource = 
            new ClassPathResource("firebase-service-account.json");

        // 2) Build FirebaseOptions with credentials
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
            .build();

        // 3) Initialize the default FirebaseApp instance, if not already initialized
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}
