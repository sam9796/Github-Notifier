# GitHub Repository Notification Bot

This project is a backend service that integrates **GitHub webhooks, Apache Kafka, and Telegram Bot API** to deliver real-time notifications about repository activity directly to users on Telegram.

Users can connect their GitHub account through OAuth, select repositories they want to monitor, and receive automated notifications in Telegram whenever important events occur in those repositories.

The system listens for GitHub webhook events such as commits and pull request activity, processes them asynchronously through Kafka, and dispatches notifications to all subscribed users via the Telegram bot.

The architecture is event-driven to ensure scalability and reliability when handling multiple repositories and high event throughput.

### Key Capabilities

- Connect a GitHub account securely using OAuth.
- Browse and subscribe to GitHub repositories through a Telegram bot interface.
- Automatically create GitHub webhooks for subscribed repositories.
- Receive real-time Telegram notifications for repository activity such as commits and pull requests.
- Process webhook events asynchronously using Kafka to decouple event ingestion and notification delivery.

### Architecture Overview

GitHub Webhook  
→ Spring Boot Webhook API  
→ Kafka Event Stream  
→ Kafka Consumer  
→ Telegram Bot Notification

This architecture allows the system to handle high volumes of repository events while maintaining reliable delivery of notifications to users.