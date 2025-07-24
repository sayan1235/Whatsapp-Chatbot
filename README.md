# WhatsApp Navigation Chatbot

A Spring Boot application that provides navigation assistance through WhatsApp Business API integration.

## Features

- **WhatsApp Integration**: Handles incoming messages via webhook
- **Navigation Assistance**: Helps users find nearby places (hospitals, restaurants, banks, etc.)
- **Firebase Integration**: Stores chat messages and user interactions
- **RESTful APIs**: Clean API structure for extensibility
- **Production Ready**: Configured for deployment on Render

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Firebase Firestore**
- **WhatsApp Business API**
- **Maven**

## Quick Start

### Prerequisites

1. Java 17 or higher
2. Maven 3.6+
3. WhatsApp Business Account
4. Firebase Project
5. Render Account (for deployment)

### Setup

1. **Run Locally**
   ```bash
   mvn spring-boot:run
   ```

2. **Configure Environment Variables**
   ```
   WHATSAPP_ACCESS_TOKEN=your_whatsapp_access_token
   WHATSAPP_VERIFY_TOKEN=your_verify_token
   WHATSAPP_PHONE_NUMBER_ID=your_phone_number_id
   FIREBASE_PROJECT_ID=your_firebase_project_id
   FIREBASE_CREDENTIALS_JSON={"type":"service_account",...}
   ```

### Deployment on Render

1. Create Web Service on Render
2. Set build command: `mvn clean package`
3. Set start command: `java -jar target/chatbot-1.0.0.jar`
4. Add environment variables

## Bot Commands

- **"hospitals near me"** - Shows nearby hospitals
- **"restaurants"** - Lists nearby restaurants  
- **"banks"** or **"ATM"** - Shows banks and ATMs
- **"gas station"** - Shows fuel stations
- **"help"** - Shows available commands
