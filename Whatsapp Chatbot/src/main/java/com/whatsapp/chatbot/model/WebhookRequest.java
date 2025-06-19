package com.whatsapp.chatbot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookRequest {
    private String object;
    private List<Entry> entry;

    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }
    
    public List<Entry> getEntry() { return entry; }
    public void setEntry(List<Entry> entry) { this.entry = entry; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entry {
        private String id;
        private List<Change> changes;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public List<Change> getChanges() { return changes; }
        public void setChanges(List<Change> changes) { this.changes = changes; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Change {
        private Value value;
        private String field;

        public Value getValue() { return value; }
        public void setValue(Value value) { this.value = value; }
        
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Value {
        private String messaging_product;
        private Object metadata;
        private List<Object> contacts;
        private List<Object> messages;

        public String getMessaging_product() { return messaging_product; }
        public void setMessaging_product(String messaging_product) { this.messaging_product = messaging_product; }
        
        public Object getMetadata() { return metadata; }
        public void setMetadata(Object metadata) { this.metadata = metadata; }
        
        public List<Object> getContacts() { return contacts; }
        public void setContacts(List<Object> contacts) { this.contacts = contacts; }
        
        public List<Object> getMessages() { return messages; }
        public void setMessages(List<Object> messages) { this.messages = messages; }
    }
}