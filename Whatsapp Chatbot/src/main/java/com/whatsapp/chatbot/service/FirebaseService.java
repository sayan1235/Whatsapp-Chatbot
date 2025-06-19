package com.whatsapp.chatbot.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentReference;
import com.whatsapp.chatbot.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseService {

    @Autowired
    private Firestore firestore;

    public void saveMessage(ChatMessage message) {
        try {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("id", message.getId());
            messageData.put("from", message.getFrom());
            messageData.put("to", message.getTo());
            messageData.put("text", message.getText());
            messageData.put("timestamp", message.getTimestamp());
            messageData.put("type", message.getType());
            messageData.put("createdAt", System.currentTimeMillis());

            DocumentReference docRef = firestore.collection("messages").document();
            docRef.set(messageData);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserInteraction(String userId, String query, String response) {
        try {
            Map<String, Object> interaction = new HashMap<>();
            interaction.put("userId", userId);
            interaction.put("query", query);
            interaction.put("response", response);
            interaction.put("timestamp", System.currentTimeMillis());

            firestore.collection("user_interactions").add(interaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}