# 💌 Romantic Message Sender

A web application that generates romantic messages using claude-ai based on book themes, converts them to speech using Amazon Polly, and sends them as voice messages via Twilio.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## 🌟 Features

- **AI-Generated Messages**: Uses Claude AI (Anthropic) to generate romantic messages based on custom book themes
- **Text-to-Speech**: Converts messages to natural-sounding voice using Amazon Polly
- **MMS Delivery**: Sends voice messages to any phone number via Twilio
- **Real-Time Pipeline**: Beautiful web UI shows all stages in real-time with progress tracking
- **Asynchronous Processing**: Non-blocking API with job status polling
- **Error Handling**: Comprehensive error messages and failure detection

## 🎨 Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.5.9**
- **Spring Async** for background processing
- **Maven** for dependency management

### AI & Cloud Services
- **Claude AI** (Anthropic) - Message generation
- **Amazon Polly** - Text-to-Speech conversion
- **Amazon S3** - Audio file storage
- **Twilio** - SMS/MMS delivery

### Frontend
- **Bootstrap 5** - Responsive UI
- **Vanilla JavaScript** - Real-time polling
- **Bootstrap Icons** - Beautiful stage icons

## 🚀 Live Demo

🔗 **[Try it here!](https://your-app.onrender.com)**

## 📸 How It Works

### Pipeline Progress
Real-time visualization of all 9 stages:
1. ⏳ Job Requested
2. 🤖 Generating Message (Claude AI)
3. ✅ Message Generated
4. 🔊 Converting to Audio (Polly)
5. 🎵 Audio Converted
6. ☁️ Uploading to S3
7. ✅ Upload Complete
8. 📤 Sending MMS (Twilio)
9. ✅ Completed

## 🏗️ Architecture

```
User Input (Phone + Theme)
    ↓
POST /api/v1/messages/send (Returns Job ID)
    ↓
Async Pipeline Execution:
    1. Claude API → Generate Message
    2. Amazon Polly → Convert to Speech
    3. Amazon S3 → Upload Audio
    4. Twilio → Send MMS
    ↓
GET /api/v1/messages/status/{jobId} (Poll for updates)
    ↓
Real-time UI Updates
```

## 🛠️ Local Setup

### Prerequisites
- Java 17+ (for Maven option)
- Maven 3.8+ (for Maven option)
- **OR** Docker & Docker Compose (recommended)
- Claude API key ([get it here](https://console.anthropic.com))
- AWS account with Polly & S3 access
- Twilio account ([free trial](https://www.twilio.com/try-twilio))

### Option 1: Docker Compose (Recommended) 🐳

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/romantic-messenger.git
   cd romantic-messenger/romanticmessenger/romanticmessenger
   ```

2. **Create `.env` file**
   ```bash
   cp .env.example .env
   # Edit .env with your API keys
   ```

3. **Start with Docker Compose**
   ```bash
   docker-compose up
   ```

4. **Open in browser**
   ```
   http://localhost:8080
   ```

See [DOCKER.md](romanticmessenger/romanticmessenger/DOCKER.md) for detailed Docker documentation.

### Option 2: Maven

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/romantic-messenger.git
   cd romantic-messenger/romanticmessenger/romanticmessenger
   ```

2. **Create `.env` file**
   ```env
   CLAUDE_API_KEY=sk-ant-api03-your-key-here
   CLAUDE_MODEL=claude-haiku-4-5-20251001
   CLAUDE_MAX_TOKENS=400

   AWS_ACCESS_KEY_ID=your-access-key
   AWS_SECRET_ACCESS_KEY=your-secret-key
   AWS_POLLY_REGION=eu-west-1
   AWS_S3_REGION=eu-north-1
   AWS_S3_BUCKET_NAME=your-bucket-name

   TWILIO_ACCOUNT_SID=your-account-sid
   TWILIO_AUTH_TOKEN=your-auth-token
   TWILIO_PHONE_NUMBER=+1234567890
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Open in browser**
   ```
   http://localhost:8080
   ```

## 📦 Deployment

- **Docker**: See [DOCKER.md](romanticmessenger/romanticmessenger/DOCKER.md) for Docker deployment
- **Cloud**: See [DEPLOYMENT.md](DEPLOYMENT.md) for Render.com, Railway.app, or Heroku

## 🔌 API Documentation

### Send Message
```http
POST /api/v1/messages/send
Content-Type: application/json

{
  "phoneNumber": "+1234567890",
  "bookTheme": "A passionate love story set in Paris during spring"
}
```

**Response (202 Accepted):**
```json
{
  "jobId": "uuid-string",
  "currentStage": "REQUESTED",
  "message": "Job created successfully...",
  "timestamp": "2026-01-07T10:00:00",
  "statusEndpoint": "/api/v1/messages/status/uuid-string"
}
```

### Get Job Status
```http
GET /api/v1/messages/status/{jobId}
```

**Response (200 OK):**
```json
{
  "jobId": "uuid-string",
  "currentStage": "COMPLETED",
  "progressPercentage": 100,
  "stageDescription": "Message sent successfully",
  "romanticMessage": "Your eyes like the summer sun...",
  "audioUrl": "https://bucket.s3.region.amazonaws.com/...",
  "recipientPhoneNumber": "+1234567890"
}
```

## 📄 License

This project is licensed under the MIT License.

---

Made with ❤️ 
