# WhatsApp Chatbot Demo

A simple Java Spring Boot application that implements a two‑step WhatsApp chat flow using:

* **Meta WhatsApp Cloud API** for messaging
* **Firebase Firestore** for session storage
* **Render.com** for deployment

---

## 🌟 Features

* **Webhook verification** with Meta’s `hub.challenge` handshake
* **Incoming message parsing** from nested JSON payloads
* **Two‑step conversation**: ask for user’s name, then greet and echo
* **Firestore-backed sessions** to maintain state per user
* **Dockerized** for consistent deployments

---

## 🚀 Getting Started

### Prerequisites

* Java 24 JDK
* Maven 3.6+ (bundled with the project via `./mvnw`)
* Firebase service‑account JSON (for Firestore)
* WhatsApp Cloud API Access Token & Phone‑Number ID

### Clone the repository

```bash
git clone https://github.com/your-username/WhatsApp-Chatbot.git
cd WhatsApp-Chatbot
```

### Configure environment variables

Create a `.env` file (or set these in your host/Cloud) with:

```bash
# WhatsApp Cloud API
env WHATSAPP_BEARER_TOKEN="EAA6..."
env WHATSAPP_PHONE_NUMBER_ID="733695083153920"

# Firebase: full JSON of your service account key
env FIREBASE_CREDENTIALS_JSON="$(cat path/to/firebase-key.json | base64 --decode)"
```

### Run locally

```bash
cd demo
./mvnw spring-boot:run
```

* Webhook endpoint: `http://localhost:8080/webhook`

### Test the webhook

Simulate an inbound WhatsApp message:

```bash
curl -i -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "entry":[{
      "changes":[{
        "value":{
          "messages":[{
            "from":"+15556522356",
            "text":{"body":"Hi there!"}
          }]
        }
      }]
    }]
  }'
```

You should see:

```
Payload received from +15556522356: Hi there!
```

And the two‑step flow in WhatsApp:

1. **Bot →** Hi! What’s your name?
2. **You →** Alice
3. **Bot →** Nice to meet you, Alice!
4. **You →** How are you?
5. **Bot →** Hey Alice, you said: How are you?

---

## 📦 Docker

Build and run with Docker:

```bash
docker build -t whatsapp-chatbot .
docker run -e WHATSAPP_BEARER_TOKEN=$WHATSAPP_BEARER_TOKEN \
           -e WHATSAPP_PHONE_NUMBER_ID=$WHATSAPP_PHONE_NUMBER_ID \
           -e FIREBASE_CREDENTIALS_JSON="$FIREBASE_CREDENTIALS_JSON" \
           -p 8080:8080 whatsapp-chatbot
```

---

## ☁️ Deployment (Render.com)

1. Create a **Web Service** on Render, connect to your GitHub repo.
2. Set **Build Command**: `./mvnw clean package`
3. Set **Start Command**: `java -jar demo/target/demo-0.0.1-SNAPSHOT.jar`
4. Add environment variables in Render Dashboard:

   * `WHATSAPP_BEARER_TOKEN`
   * `WHATSAPP_PHONE_NUMBER_ID`
   * `FIREBASE_CREDENTIALS_JSON`
5. Deploy and grab your public URL.

---

## 🛠️ Project Structure

```text
WhatsApp-Chatbot/         # Repo root
├── demo/                 # Spring Boot application
│   ├── Dockerfile
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java/com/chatbot/demo
│       │   │   ├── config
│       │   │   │   └── FirebaseAdminConfig.java
│       │   │   ├── controller
│       │   │   │   └── WebhookController.java
│       │   │   ├── model
│       │   │   │   └── UserSession.java
│       │   │   └── DemoApplication.java
│       │   └── resources
│       │       ├── application.properties
│       │       └── firebase-service-account.json  # local dev fallback
│       └── test           # Unit/integration tests
├── .env.example           # Example environment variables
└── README.md
```
