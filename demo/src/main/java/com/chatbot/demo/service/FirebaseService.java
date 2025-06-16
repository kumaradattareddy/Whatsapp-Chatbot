package com.chatbot.demo.service;

import org.springframework.stereotype.Service;

import com.chatbot.demo.model.UserSession;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirebaseService {

    private static final String COLLECTION = "sessions";

    /**
     * Load a session by phone number (doc ID).
     * Returns null if none exists.
     */
    public UserSession loadSession(String phoneNumber) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        var snap = db.collection(COLLECTION)
                     .document(phoneNumber)
                     .get()
                     .get();
        return snap.exists() ? snap.toObject(UserSession.class) : null;
    }

    /**
     * Save or update a session document keyed by phoneNumber.
     */
    public void saveSession(UserSession session) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION)
          .document(session.getPhoneNumber())
          .set(session)
          .get();
    }

   
}
