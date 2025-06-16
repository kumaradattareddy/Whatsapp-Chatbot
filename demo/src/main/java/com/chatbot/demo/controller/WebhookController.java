package com.chatbot.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.demo.model.UserSession;
import com.chatbot.demo.service.FirebaseService;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final String VERIFY_TOKEN = "mySuperSecretVerifyToken123";

    private final FirebaseService firebaseService;

    public WebhookController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    /**
     * GET /webhook
     * Used by Meta to verify your callback URL.
     */
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
        @RequestParam(name = "hub.mode", required = false) String mode,
        @RequestParam(name = "hub.verify_token", required = false) String token,
        @RequestParam(name = "hub.challenge", required = false) String challenge
    ) {
        // Only echo back the challenge if verify_token matches
        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("Verification token mismatch");
        }
    }

    /**
     * POST /webhook
     * Receives incoming WhatsApp messages, persists sessions, and replies.
     */
    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) throws Exception {
        // 1) Extract sender & message text
        String from = (String) payload.get("from");
        String text = (String) payload.get("text");
        System.out.println("Payload received from " + from + ": " + text);

        // 2) Load or initialize user session
        UserSession session = firebaseService.loadSession(from);
        if (session == null) {
            session = new UserSession(from, "ASK_NAME", Map.of());
            firebaseService.saveSession(session);
            System.out.println("Created new session for " + from);
            return ResponseEntity.ok("Hi! Whats your name?");
        }

        // 3) Route by conversation state
        switch (session.getState()) {
            case "ASK_NAME" -> {
                // User provided their name
                session.getContext().put("name", text);
                session.setState("GREETED");
                firebaseService.saveSession(session);
                return ResponseEntity.ok("Nice to meet you, " + text + "!");
            }
            case "GREETED" -> {
                // Already greeted: echo back
                String name = (String) session.getContext().get("name");
                return ResponseEntity.ok("Hey " + name + ", you said: " + text);
            }
            default -> {
                // Fallback for any other state
                return ResponseEntity.ok("Sorry, I didnt get that.");
            }
        }
    }
}
