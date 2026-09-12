# 🏦 Banking System

A secure and scalable **Banking System REST API** developed using **Spring Boot**. The application provides banking-related functionality along with authentication, authorization, transaction management, email communication, real-time WebSocket notifications, and Redis-based caching.

The project follows a layered backend architecture using Spring Boot, Spring Security, Spring Data JPA, MySQL, JWT, WebSocket, Java Mail, and Redis.

---

## 📌 Project Overview

The Banking System is designed to provide a secure backend for managing banking operations.

The application focuses on:

* 🔐 User Authentication
* 🛡️ Authorization and Role-Based Security
* 👤 User and Account Management
* 💰 Banking Transactions
* 🔑 JWT-Based Authentication
* 📩 Email Notifications
* 🔔 Real-Time WebSocket Notifications
* ⚡ Redis-Based Caching
* 🗄️ MySQL Database Integration
* ✅ Request Validation
* 🧩 Layered and Maintainable Architecture

The project is implemented as a **single Spring Boot application**, without using a microservice architecture.

---

## ✨ Features

### 🔐 Authentication & Security

* User registration and authentication
* JWT-based authentication
* Spring Security integration
* Protected REST API endpoints
* Role-based authorization
* Password security
* Stateless authentication using JWT

### 👤 User Management

* Create and manage users
* Retrieve user information
* User authentication
* User authorization based on roles

### 🏦 Banking Operations

The backend is designed to support core banking operations such as:

* Account management
* Deposits
* Withdrawals
* Fund transfers
* Balance management
* Transaction processing
* Transaction history

### 📧 Email Service

The project integrates Spring Boot Mail for sending emails.

Possible use cases include:

* Account-related notifications
* Transaction notifications
* Authentication-related emails
* Other system notifications

### 🔔 Real-Time Notifications

The application uses **WebSocket** technology for real-time communication.

This allows the server to send notifications to connected users without requiring the client to continuously poll the REST API.

Example:

```text
Banking Transaction
       ↓
Transaction Processed
       ↓
Notification Generated
       ↓
WebSocket
       ↓
User receives notification
```

### ⚡ Redis Caching

Redis is integrated into the application to improve performance by caching frequently requested data.

Spring's caching abstraction is used to simplify cache management.

Example flow:

```text
Client Request
      ↓
Controller
      ↓
Service
      ↓
Redis Cache
   ↙       ↘
 HIT       MISS
  ↓          ↓
Return    Database
Data         ↓
          Store in Cache
              ↓
          Return Data
```

### 🗄️ MySQL Database

MySQL is used as the relational database.

Database interaction is handled through:

* Spring Data JPA
* Hibernate
* JPA Repositories
* Entity classes

### ✅ Validation

The application uses Spring Boot validation to validate incoming request data and prevent invalid information from entering the system.

---

## 🛠️ Technologies Used

| Technology           | Purpose                            |
| -------------------- | ---------------------------------- |
| ☕ Java 21            | Programming Language               |
| 🌱 Spring Boot 4.0.2 | Backend Framework                  |
| 🔐 Spring Security   | Authentication & Authorization     |
| 🎫 JWT               | Token-Based Authentication         |
| 🗄️ Spring Data JPA  | Database Access                    |
| 🐬 MySQL             | Relational Database                |
| 🔴 Redis             | Caching                            |
| 🔔 WebSocket         | Real-Time Notifications            |
| 📧 Spring Boot Mail  | Email Communication                |
| ✅ Spring Validation  | Request Validation                 |
| 🧰 Maven             | Dependency Management & Build      |
| 🧩 Lombok            | Boilerplate Code Reduction         |
| 📦 Jackson           | JSON Serialization/Deserialization |

The technologies above are reflected in the project's Maven configuration.

---

## 🏗️ Architecture

The application follows a layered architecture:

```text
                    ┌─────────────────────┐
                    │      Client         │
                    │ React / Postman etc.│
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Controller      │
                    │    REST API Layer   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      Service        │
                    │   Business Logic    │
                    └──────┬───────┬──────┘
                           │       │
                ┌──────────┘       └──────────┐
                ▼                             ▼
      ┌──────────────────┐          ┌──────────────────┐
      │    Repository    │          │ External Services│
      │   JPA / MySQL    │          │ Mail / WebSocket │
      └────────┬─────────┘          └──────────────────┘
               │
               ▼
      ┌──────────────────┐
      │      MySQL       │
      │     Database     │
      └──────────────────┘

               ▲
               │
      ┌──────────────────┐
      │      Redis       │
      │      Cache       │
      └──────────────────┘
```

---

## 🔐 Authentication Flow

The application uses JWT for stateless authentication.

```text
User
 │
 │ Login Credentials
 ▼
Authentication API
 │
 ▼
Spring Security
 │
 ├── Invalid → Authentication Failed
 │
 └── Valid
       │
       ▼
   Generate JWT
       │
       ▼
   Return Token
       │
       ▼
Client stores Token
       │
       ▼
Client sends JWT
with every protected request
       │
       ▼
JWT Authentication Filter
       │
       ▼
Validate Token
       │
       ▼
Access Protected Resource
```

---

## 📂 Project Structure

The repository follows the standard Maven/Spring Boot structure:

```text
Banking-System/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── banking/
│   │   │           ├── BankingSystemApplication.java
│   │   │           │
│   │   │           ├── controller/
│   │   │           ├── service/
│   │   │           ├── repository/
│   │   │           ├── entity/
│   │   │           ├── security/
│   │   │           ├── configuration/
│   │   │           └── ...
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── .gitignore
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

> Package names and classes may evolve as the project is developed further.

---

# 🚀 Getting Started

## Prerequisites

Before running the project, install:

* Java JDK 21
* Maven
* MySQL Server
* Redis Server
* Git
* IDE such as Spring Tool Suite, IntelliJ IDEA, or Eclipse

You can verify Java installation using:

```bash
java -version
```

Verify Maven using:

```bash
mvn -version
```

---

## 📥 Clone the Repository

```bash
git clone https://github.com/HimanshuDubal/Banking-System.git
```

Navigate into the project:

```bash
cd Banking-System
```

---

## 🗄️ Database Configuration

Create a MySQL database for the application.

Example:

```sql
CREATE DATABASE banking_system;
```

Then configure the database connection inside:

```text
src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_system
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Do not commit real database passwords, JWT secrets, email passwords, or other credentials to GitHub.

---

## 🔴 Redis Configuration

Make sure Redis is running locally.

Typical configuration:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

The project includes Spring Boot's cache and Redis dependencies for caching support.

---

## 🔑 JWT Configuration

Configure the JWT secret and expiration according to your application's security configuration.

For example:

```properties
jwt.secret=YOUR_SECRET_KEY
jwt.expiration=YOUR_EXPIRATION_TIME
```

For production deployments, use environment variables or a secrets manager instead of storing secrets directly in the configuration file.

---

## 📧 Email Configuration

If email functionality is enabled, configure the mail server in:

```text
application.properties
```

Example:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

For Gmail, use an **App Password** rather than your normal Google account password.

---

# ▶️ Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using Maven Wrapper — Windows

```bash
mvnw.cmd spring-boot:run
```

### Using Maven Wrapper — Linux/macOS

```bash
./mvnw spring-boot:run
```

After successful startup, the Spring Boot application will normally be available at:

```text
http://localhost:8080
```

---

# 📦 Build the Project

To create the executable JAR:

```bash
mvn clean package
```

The generated JAR will be available inside:

```text
target/
```

You can run it using:

```bash
java -jar target/BankingSystem-0.0.1-SNAPSHOT.jar
```

---

# 🧪 API Testing

The REST APIs can be tested using tools such as:

* Postman
* Insomnia
* REST Client
* Frontend applications

Typical request flow:

```text
Register
   ↓
Login
   ↓
Receive JWT
   ↓
Send JWT in Authorization Header
   ↓
Access Protected APIs
```

For protected endpoints, the token is generally sent using:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

# 🔔 WebSocket Notification Flow

The application uses WebSocket for real-time user notifications.

```text
                    Banking System
                          │
                          │ Transaction
                          ▼
                  Transaction Service
                          │
                          ▼
                  Notification Event
                          │
                          ▼
                      WebSocket
                          │
                          ▼
                    Connected User
```

This makes it possible to notify users immediately when relevant banking events occur.

---

# ⚡ Caching Flow

Redis is used as a cache layer for frequently accessed data.

```text
              Client Request
                    │
                    ▼
                Controller
                    │
                    ▼
                 Service
                    │
                    ▼
               Check Cache
                 /     \
              HIT       MISS
               │          │
               ▼          ▼
          Return Data   Database
                           │
                           ▼
                       Redis Cache
                           │
                           ▼
                       Return Data
```

Caching can reduce unnecessary database queries and improve response time for suitable read operations.

---

# 🛡️ Security

Security is an important part of the project.

The application uses:

* Spring Security
* JWT Authentication
* Authorization
* Password encryption
* Protected REST endpoints
* Request validation
* Stateless authentication

The project's Maven configuration includes Spring Security, JWT libraries, validation, and other security-related dependencies.

---

# 🧩 Design Approach

The project follows a **layered architecture** rather than a microservice architecture.

### Controller Layer

Responsible for:

* Receiving HTTP requests
* Validating request data
* Returning HTTP responses

### Service Layer

Responsible for:

* Business logic
* Transaction processing
* Authentication-related operations
* Communication with repositories and other services

### Repository Layer

Responsible for:

* Database operations
* Entity persistence
* Query execution

### Security Layer

Responsible for:

* Authentication
* JWT validation
* Authorization
* Securing endpoints

### Infrastructure Services

The application also integrates:

* MySQL
* Redis
* WebSocket
* Email service

---

# 🎯 Project Objectives

The main objectives of this project are:

1. Build a secure banking backend using Spring Boot.
2. Implement authentication and authorization using Spring Security.
3. Use JWT for stateless authentication.
4. Manage banking data using MySQL and JPA.
5. Implement real-time notifications using WebSocket.
6. Improve application performance using Redis caching.
7. Implement email communication.
8. Follow a clean layered architecture.
9. Build a backend that can be consumed by web or mobile frontend applications.
10. Gain practical experience with modern Spring Boot backend development.

---

# 🔮 Future Enhancements

The project can be extended with:

* 📱 React/Angular frontend
* 💳 Debit/Credit Card management
* 🧾 PDF transaction statements
* 📊 Financial analytics
* 🔔 Advanced notification preferences
* 🏦 Multiple bank account types
* 💸 Scheduled payments
* 📱 Mobile application
* 🧑‍💼 Admin management portal
* 📈 Advanced transaction reports
* 🐳 Docker deployment
* ☁️ Cloud deployment
* 🧪 Comprehensive unit and integration testing
* 📖 Swagger/OpenAPI documentation

---

# 🤝 Contributing

Contributions are welcome.

### 1. Fork the repository

```bash
git fork
```

### 2. Clone your fork

```bash
git clone <your-fork-url>
```

### 3. Create a new branch

```bash
git checkout -b feature/new-feature
```

### 4. Make your changes

Implement and test your changes.

### 5. Commit your changes

```bash
git add .
git commit -m "Add new feature"
```

### 6. Push the branch

```bash
git push origin feature/new-feature
```

### 7. Create a Pull Request

Open a Pull Request on GitHub.

---

# ⚠️ Disclaimer

This project is developed for **educational and demonstration purposes**.

It should not be considered production-ready banking software without additional security auditing, testing, monitoring, compliance controls, and infrastructure hardening.

---

# 👨‍💻 Author

**Himanshu Dubal**

Computer Engineering Student

GitHub: [HimanshuDubal](https://github.com/HimanshuDubal)

---

# ⭐ Support

If you find this project useful, consider giving the repository a ⭐ on GitHub.

**Repository:**
https://github.com/HimanshuDubal/Banking-System

---

## 📄 License

This project is currently intended primarily as an educational project. Add an appropriate open-source license such as MIT if you want others to reuse and modify the code under defined terms.
