# 🎮 Game Leaderboard API

A REST API for managing game leaderboards with JWT-based authentication and role-based authorization.

[![Java](https://img.shields.io/badge/Java-24-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## 📋 Overview

This project is a learning exercise focused on:
- Spring Boot with JWT-based security
- Test-Driven Development (TDD)
- CI/CD with GitHub Actions
- API testing with Postman and TestRail

**Key Features:**
- 🔐 JWT Authentication
- 👥 Role-Based Authorization (ADMIN, USER)
- 🎮 Player Management
- 🏆 Leaderboard System
- ✅ 80%+ Test Coverage

---

## 🚀 Quick Start

### Prerequisites
- Java 24+
- Maven 3.8+
- MySQL 8.0+ (for production)

### Installation & Run
```bash
# Clone repository
git clone https://github.com/yourusername/game-leaderboard-api.git
cd game-leaderboard-api

# Build project
mvn clean install

# Run (development mode with H2)
mvn spring-boot:run
```
```
Default Admin Credentials:
Username: t430
Password: Dummy#07
Base URL: http://localhost:8080
```

---
## 📚 Documentation

- [Architecture Documentation](docs/architecture.md) - Complete system design, 
API endpoints, database schema, testing strategy.

- [API Documentation (Postman)](https://documenter.getpostman.com/view/46122428/2sB3QKtAqM) - Interactive API 
documentation with examples.
- [Development Log](docs/development-log.md) - Development notes and decisions
- [Troubleshooting Guide](docs/troubleshooting-log.md) - Common issues and solutions

---
## 🧪 Testing

### System Tests (Java)
```bash
# Run all tests
mvn clean test
```
#### Test Types:
- Unit Tests - Service layer business logic (JUnit 5 + Mockito)
- Slice Tests - Controller endpoints with MockMvc
- Security Tests - Authentication and authorization flows
- Repository Tests - Data access layer integration

### API Tests (Postman)
Postman Collection: [Game Leaderboard API](https://documenter.getpostman.com/view/46122428/2sB3QKtAqM)

#### Test Categories:
- **🔐 Authentication** - Login, registration
- **👥 User Management** - CRUD operations, role-based access
- **🎮 Player Management** - Profile operations, validation rules, token validation
- **🏆 Leaderboard** - Rankings accuracy, data integrity
- **❌ Account Viewer** - All accounts, specific accounts, leaderboard view


#### Test Instruction (Test All Request)

```
**Take Note:** Ensure that the program and the database is connected before conducting testing.
```

1) Run `Positive(Create): Login - Base admin user` request.
   **Location:** Authentication > Login: Authenticated user

2) Run `Delete(Admin): Clean up all users` request twice.
   **Location:** Utilities

3) Run `Game Leaderboard API` collections.

---

## 🛠️ Tech Stack
Java 24 • Spring Boot • Spring Security • JWT • Maven • JUnit 5 • Mockito • H2/MySQL • GitHub Actions • Postman

---

## 👤 Author
### Marlon B
- GitHub: https://github.com/Acrexia07
- LinkedIn: https://www.linkedin.com/in/marlon-barnuevo/

---
_⭐ If you find this project helpful, please give it a star!_

