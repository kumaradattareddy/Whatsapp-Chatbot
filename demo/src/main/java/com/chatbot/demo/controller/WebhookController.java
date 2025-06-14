package com.chatbot.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    @PostMapping
    public ResponseEntity<String> receive(@RequestBody String payload) {
        System.out.println("Payload received: " + payload);
        return ResponseEntity.ok("ACK");
    }
}

