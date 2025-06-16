package com.chatbot.demo.model;

import java.util.Map;

public class UserSession {

    private String phoneNumber;
    
    private String state;

    private Map<String, Object> context;
   
    public UserSession() {}

    public UserSession(String phoneNumber, String state, Map<String, Object> context) {
        this.phoneNumber = phoneNumber;
        this.state       = state;
        this.context     = context;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }
}
