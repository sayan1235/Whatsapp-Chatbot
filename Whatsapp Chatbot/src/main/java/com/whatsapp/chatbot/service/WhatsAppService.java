package com.whatsapp.chatbot.service;

import com.whatsapp.chatbot.model.WebhookRequest;
import com.whatsapp.chatbot.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    @Autowired
    private FirebaseService firebaseService;

    @Value("${whatsapp.access.token}")
    private String accessToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processIncomingMessage(WebhookRequest request) {
        if (request.getEntry() != null && !request.getEntry().isEmpty()) {
            request.getEntry().forEach(entry -> {
                if (entry.getChanges() != null && !entry.getChanges().isEmpty()) {
                    entry.getChanges().forEach(change -> {
                        if (change.getValue() != null && change.getValue().getMessages() != null) {
                            change.getValue().getMessages().forEach(message -> {
                                handleMessage(message, change.getValue().getContacts());
                            });
                        }
                    });
                }
            });
        }
    }

    private void handleMessage(Map<String, Object> message, Object contacts) {
        String from = (String) message.get("from");
        String messageId = (String) message.get("id");
        String timestamp = (String) message.get("timestamp");
        
        // Extract message text
        String messageText = "";
        if (message.get("text") != null) {
            Map<String, Object> textObj = (Map<String, Object>) message.get("text");
            messageText = (String) textObj.get("body");
        }

        // Create chat message object
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(messageId);
        chatMessage.setFrom(from);
        chatMessage.setText(messageText);
        chatMessage.setTimestamp(timestamp);
        chatMessage.setType("incoming");

        // Save to Firebase
        firebaseService.saveMessage(chatMessage);

        // Process navigation request
        String response = processNavigationQuery(messageText);
        
        // Send response
        sendMessage(from, response);
    }

    private String processNavigationQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "Hello! I'm your navigation assistant. Please tell me where you'd like to go.";
        }

        query = query.toLowerCase().trim();

        // Simple navigation responses
        if (query.contains("hospital") || query.contains("medical")) {
            return "🏥 Here are nearby hospitals:\n1. City General Hospital - 2.3 km\n2. Metro Medical Center - 3.1 km\n3. Emergency Care Clinic - 1.8 km\n\nWould you like directions to any of these?";
        } else if (query.contains("restaurant") || query.contains("food")) {
            return "🍽️ Popular restaurants nearby:\n1. The Food Corner - 0.8 km\n2. Spice Garden - 1.2 km\n3. Quick Bites - 0.5 km\n\nWhich one interests you?";
        } else if (query.contains("bank") || query.contains("atm")) {
            return "🏦 Nearby banks and ATMs:\n1. National Bank - 0.6 km\n2. ATM - Corner Street - 0.3 km\n3. City Bank Branch - 1.1 km\n\nNeed directions to any of these?";
        } else if (query.contains("gas") || query.contains("petrol") || query.contains("fuel")) {
            return "⛽ Nearby fuel stations:\n1. Shell Station - 0.7 km\n2. BP Petrol Pump - 1.4 km\n3. Indian Oil - 0.9 km\n\nWhich one would you like directions to?";
        } else if (query.contains("direction") || query.contains("how to reach")) {
            return "📍 I can help you with directions! Please specify:\n- Your destination\n- Your preferred mode of transport (car, walk, public transport)\n\nExample: 'Directions to City Mall by car'";
        } else if (query.contains("help") || query.contains("commands")) {
            return "🤖 I can help you with:\n• Finding nearby places (restaurants, hospitals, banks, etc.)\n• Getting directions\n• Navigation assistance\n\nJust tell me what you're looking for!";
        } else {
            return "🗺️ I'm searching for '" + query + "' in your area. Could you be more specific? For example:\n- 'Restaurants near me'\n- 'Directions to [place name]'\n- 'Nearby hospitals'\n\nType 'help' for more options.";
        }
    }

    public void sendMessage(String to, String message) {
        try {
            String url = "https://graph.facebook.com/v18.0/" + phoneNumberId + "/messages";

            Map<String, Object> payload = new HashMap<>();
            payload.put("messaging_product", "whatsapp");
            payload.put("to", to);
            payload.put("type", "text");
            
            Map<String, String> text = new HashMap<>();
            text.put("body", message);
            payload.put("text", text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            // Save sent message to Firebase
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setFrom("bot");
            chatMessage.setTo(to);
            chatMessage.setText(message);
            chatMessage.setTimestamp(String.valueOf(System.currentTimeMillis()));
            chatMessage.setType("outgoing");
            firebaseService.saveMessage(chatMessage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}