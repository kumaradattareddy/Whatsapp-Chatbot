package com.chatbot.demo.model;

import java.util.Map;

public class UserSession {

    /** 
     * The user’s phone number (we’ll use it as the document ID). 
     */
    private String phoneNumber;

    /**
     * Current state of the conversation (e.g., “ASK_NAME”, “GREETED”).
     */
    private String state;

    /**
     * Any contextual data you need (e.g., name they provided).
     */
    private Map<String, Object> context;

    // Constructors
    public UserSession() {}

    public UserSession(String phoneNumber, String state, Map<String, Object> context) {
        this.phoneNumber = phoneNumber;
        this.state       = state;
        this.context     = context;
    }

    // Getters & setters
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }
}
