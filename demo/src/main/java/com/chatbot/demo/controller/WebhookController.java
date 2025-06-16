package com.chatbot.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.demo.model.UserSession;
import com.chatbot.demo.service.FirebaseService;

/**
 * A simple WhatsApp webhook controller that uses Firestore-backed sessions
 * to manage a two-step “ask name → greet” conversation flow.
 */
@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final FirebaseService firebaseService;

    public WebhookController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping
    public ResponseEntity<String> receive(@RequestBody Map<String, Object> payload) throws Exception {
        // 1) Extract sender & message text from the incoming payload
        String from = (String) payload.get("from");
        String text = (String) payload.get("text");

        System.out.println("Payload received from " + from + ": " + text);

        // 2) Load or initialize user session
        UserSession session = firebaseService.loadSession(from);
        if (session == null) {
            // New user: ask for their name
            session = new UserSession(from, "ASK_NAME", Map.of());
            firebaseService.saveSession(session);
            System.out.println("Created new session for " + from);
            return ResponseEntity.ok("Hi! Whats your name?");
        }

        // 3) Route by conversation state
        return switch (session.getState()) {
            case "ASK_NAME" -> {
                // User provided their name
                session.getContext().put("name", text);
                session.setState("GREETED");
                firebaseService.saveSession(session);
                yield ResponseEntity.ok("Nice to meet you, " + text + "!");
            }
            case "GREETED" -> {
                // Already greeted: echo back
                String name = (String) session.getContext().get("name");
                yield ResponseEntity.ok("Hey " + name + ", you said: " + text);
            }
            default -> {
                // Fallback for any other state
                yield ResponseEntity.ok("Sorry, I didnt get that.");
            }
        };
    }
}
