# 🧱 Architecture Overview – Game Leaderboard API

---

## 1. Project Purpose
- Project Goal: Provide a REST API that views a simple game leaderboard.
- Learning Goal:
    - Strengthen Java fundamentals (Collections and Streams), apply Spring Boot with JWT-based security, 
      and practice CI/CD using GitHub Actions.
    - To acquire knowledge in JWT implementation.
    - To acquire QA knowledge in Postman Automation testing and Test Management tools using TestRail.

## 2. Tech Stack
| Component       | Tech Used                                           |
|-----------------|-----------------------------------------------------|
| Language        | Java                                                |
| Framework       | Spring Boot                                         |
| Build Tool      | Maven                                               |
| Dependencies    | Spring Web, Spring Security, Lombok, MapStruct, JWT |
| Testing Tools   | JUnit 5, Mockito, Postman, TestRail                 |
| CI/CD           | GitHub Actions                                      |
| Database (Dev)  | H2                                                  |
| Database (Prod) | MySQL                                               |
| Auth            | JWT                                                 |

## 3. System Design

### 3.1. Data Model

#### 3.1.a Player
| Field        | Type          | Validation                                |
|--------------|---------------|-------------------------------------------|
| `id`         | long          | Auto-generated (no need to validate)      |
| `uuid`       | UUID          | Auto-generated                            |
| `playerName` | String        | Not empty, max 10 characters              |
| `scores`     | int           | Must be present                           |
| `gameRank`   | int           | Can be null                               |
| `timestamp`  | LocalDateTime | Auto-generated (Local date and time: NOW) |
| `userId`     | long          | Foreign key to User                       |

#### 3.1.b User
| Field       | Type            | Validation                                                           |
|-------------|-----------------|----------------------------------------------------------------------|
| `id`        | long            | Auto-generated (no need to validate)                                 |
| `username`  | String          | Not empty, max 6 characters                                          |
| `password`  | String          | Not empty, must follow pattern, min 8 characters, max 12 characters  |
| `role`      | UserRole object | Not be empty, allows 'ADMIN' and 'USER' only                         |
| `createdAt` | LocalDateTime   | Auto-generated (Local date and time: NOW)                            |
| `player`    | PlayerEntity    | Mapped by user entity (One-to-One relationship)                      |

**Relationships:**
- User has one Player (One-to-One)
- Player belongs to one User

### 3.2. API Endpoints

#### 🔐 Authentication

##### Token Generation
| Method | Path                   | Description                    | Auth Required |
|--------|------------------------|--------------------------------|---------------|
| POST   | `/api/auth/tokens`     | Generate token for setup       | No            |

##### Login: Authenticated User
| Method | Path                   | Description                                 | Auth Required |
|--------|------------------------|---------------------------------------------|---------------|
| POST   | `/api/auth/login`      | Login - Base admin user                     | No            |
| POST   | `/api/auth/login`      | Login - New admin user                      | No            |
| POST   | `/api/auth/login`      | Login - Regular user with player account    | No            |
| POST   | `/api/auth/login`      | Login - Regular user without player account | No            |

**Negative Test Cases:**
- POST `/api/auth/login` - Invalid credentials
- POST `/api/auth/login` - Missing field
- POST `/api/auth/login` - Malformed JSON

##### Registration: Regular User
| Method | Path                   | Description                    | Auth Required |
|--------|------------------------|--------------------------------|---------------|
| POST   | `/api/auth/register`   | Register regular user          | No            |

**Negative Test Cases:**
- POST `/api/auth/register` - Invalid username
- POST `/api/auth/register` - Invalid password
- POST `/api/auth/register` - Missing field
- POST `/api/auth/register` - Malformed JSON

##### Registration: Admin User
| Method | Path                       | Description                   | Auth Required |
|--------|----------------------------|-------------------------------|---------------|
| POST   | `/api/auth/register/admin` | Register admin                | Yes (Admin)   |

**Negative Test Cases:**
- POST `/api/auth/register/admin` - Invalid username
- POST `/api/auth/register/admin` - Invalid password
- POST `/api/auth/register/admin` - Invalid role
- POST `/api/auth/register/admin` - Missing field
- POST `/api/auth/register/admin` - Malformed JSON

---

#### 👥 User Management

##### Self-Endpoints (User operations on their own account)
| Method | Path                   | Description                    | Auth Required    |
|--------|------------------------|--------------------------------|------------------|
| PUT    | `/api/users/profile`   | Update user profile            | Yes (User/Admin) |

**Negative Test Cases:**
- GET `/api/users/profile` - Invalid token
- PUT `/api/users/profile` - Invalid username
- PUT `/api/users/profile` - Invalid password
- PUT `/api/users/profile` - Missing field
- PUT `/api/users/profile` - Malformed JSON

##### Admin-Endpoints (Admin operations on any user)
| Method | Path                   | Description                    | Auth Required |
|--------|------------------------|--------------------------------|---------------|
| PUT    | `/api/users/{id}`      | Update user by ID (Admin)      | Yes (Admin)   |
| DELETE | `/api/users/{id}`      | Delete user by ID (Admin)      | Yes (Admin)   |

**Negative Test Cases:**
- GET `/api/users/{id}` - User ID not exists
- PUT `/api/users/{id}` - Invalid username
- PUT `/api/users/{id}` - Invalid password
- PUT `/api/users/{id}` - Missing field
- PUT `/api/users/{id}` - Malformed JSON
- DELETE `/api/users/{id}` - User ID not exists

---

#### 🎮 Player Management

##### Self-Endpoints (Player operations on their own account)
| Method | Path                   | Description                    | Auth Required    |
|--------|------------------------|--------------------------------|------------------|
| GET    | `/api/players`         | Get player profile             | Yes (User/Admin) |
| PUT    | `/api/players`         | Update player profile          | Yes (User/Admin) |
| POST   | `/api/players`         | Create new player              | Yes (User/Admin) |

**Negative Test Cases:**
- POST `/api/players` - Invalid player name
- POST `/api/players` - Invalid score
- POST `/api/players` - Missing field
- POST `/api/players` - Malformed JSON
- POST `/api/players` - Creating multiple player accounts (not allowed)
- PUT `/api/players` - Invalid player name
- PUT `/api/players` - Invalid score
- PUT `/api/players` - Missing field
- PUT `/api/players` - Malformed JSON
- PUT `/api/players` - Player account not exists
- GET `/api/players` - Player account not exists

##### Admin-Endpoints (Admin operations on any player)
| Method | Path                   | Description                    | Auth Required |
|--------|------------------------|--------------------------------|---------------|
| PUT    | `/api/players/{id}`    | Update player by ID (Admin)    | Yes (Admin)   |

**Negative Test Cases:**
- GET `/api/players/{id}` - Player ID not exists
- PUT `/api/players/{id}` - Invalid player name
- PUT `/api/players/{id}` - Invalid score
- PUT `/api/players/{id}` - Missing field
- PUT `/api/players/{id}` - Malformed JSON

---

#### 📊 Account Viewer

##### All Accounts
| Method | Path                   | Description                    | Auth Required |
|--------|------------------------|--------------------------------|---------------|
| GET    | `/api/players/all`     | Get all players (Admin)        | Yes (Admin)   |
| GET    | `/api/users/all`       | Get all users (Admin)          | Yes (Admin)   |

##### Specific Accounts
| Method | Path                   | Description                    | Auth Required    |
|--------|------------------------|--------------------------------|------------------|
| GET    | `/api/users/profile`   | Get user profile               | Yes (User/Admin) |
| GET    | `/api/players/{id}`    | Get player by ID (Admin)       | Yes (Admin)      |
| GET    | `/api/users/{id}`      | Get user by ID (Admin)         | Yes (Admin)      |

---

#### 🏆 Leaderboard
| Method | Path                   | Description                    | Auth Required    |
|--------|------------------------|--------------------------------|------------------|
| GET    | `/api/leaderboards`    | Get top 3 players              | Yes (User/Admin) |

---

#### 🛠️ Utilities
| Method | Path                   | Description                          | Auth Required |
|--------|------------------------|--------------------------------------|---------------|
| DELETE | `/api/users/cleanup`   | Clean up all users except base admin | Yes (Admin)   |

---

## 4. Architecture Layers
┌─────────────────┐
│   Controller    │ ← Handles HTTP requests, Role-based authorization
├─────────────────┤
│    Service      │ ← Business logic 
├─────────────────┤
│   Repository    │ ← Data access layer (JPA)
├─────────────────┤
│    Database     │ ← H2 (Test) / MySQL (Local)
└─────────────────┘

**Flow:** Controller → Service → Repository → Database

**Request/Response Flow:**
1. Client sends HTTP request with JWT token (if required)
2. Spring Security validates JWT token
3. Controller receives request and validates input
4. Service layer processes business logic
5. Repository layer interacts with database
6. Response returns through the same layers

---

## 5. Security

### Authentication & Authorization
- **Type:** JWT (JSON Web Token)
- **Token Expiry:** 24 hours (configurable)
- **Password Storage:** BCrypt hashing
- **Role-based Access:** ADMIN, USER

### Roles & Permissions
| Role  | Permissions                                                    |
|-------|----------------------------------------------------------------|
| ADMIN | Full access to all endpoints, can manage all users and players |
| USER  | Can view leaderboard, manage own profile and player account    |

### Secured Endpoints
- **Public Endpoints:** Login, Registration (Regular User), Token Generation
- **User Endpoints:** Require valid JWT, user can only access their own data
- **Admin Endpoints:** Require valid JWT + ADMIN role

### Token Types (for Testing)
- `BaseAdminToken` - Initial admin operations
- `NewAdminToken` - New admin operations
- `UserWithNoPlayerAccountToken` - User without player account
- `UserWithPlayerAccountToken` - User with existing player account

---

## 6. Project Structure

```
game-leaderboard-api/
├── .github/
│   └── workflows/
│       └── ci.yml          
│
├── docs/
│   ├── architecture.md               # Architecture documentation
│   ├── development-log.md            # Development notes
│   └── troubleshooting-log.md        # Issues and solutions
│
├── src/
│   ├── main/
│   │   ├── java/com/marlonb/game_leaderboard_api/
│   │   │   ├── controller/
│   │   │   │   ├── PlayerController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── ApiMessageResponseDto.java
│   │   │   │   └── enum_values/
│   │   │   │       ├── PlayerApiSuccessfulMessages.java
│   │   │   │       └── UserApiSuccessfulMessages.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── PlayerService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── JWTService.java
│   │   │   │   ├── GameUserDetailsService.java
│   │   │   │   └── ServiceErrorMessages.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── PlayerRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── PlayerEntity.java
│   │   │   │   ├── PlayerRequestDto.java
│   │   │   │   ├── PlayerResponseDto.java
│   │   │   │   ├── PlayerSummaryDto.java
│   │   │   │   ├── PlayerUpdateDto.java
│   │   │   │   ├── PlayerInfoMapper.java
│   │   │   │   └── user/
│   │   │   │       ├── UserEntity.java
│   │   │   │       ├── UserRequestDto.java
│   │   │   │       ├── UserResponseDto.java
│   │   │   │       ├── UserSummaryDto.java
│   │   │   │       ├── UserUpdateDto.java
│   │   │   │       ├── AdminUserRequestDto.java
│   │   │   │       ├── AdminUserUpdateDto.java
│   │   │   │       ├── LoginRequestDto.java
│   │   │   │       ├── JwtResponseDto.java
│   │   │   │       ├── UserPrincipal.java
│   │   │   │       ├── UserMapper.java
│   │   │   │       └── UserRoles.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── JwtFilter.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ErrorResponseDto.java
│   │   │   │   ├── CustomAuthenticationEntryPoint.java
│   │   │   │   ├── custom/
│   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   └── DuplicateResourceFoundException.java
│   │   │   │   └── enum_values/
│   │   │   │       ├── ErrorMessages.java
│   │   │   │       ├── AuthenticationErrorMessages.java
│   │   │   │       ├── AuthenticationStringValues.java
│   │   │   │       ├── HttpClientErrorMessage.java
│   │   │   │       └── ResponseKey.java
│   │   │   │
│   │   │   └── GameLeaderboardApiApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-local.properties
│   │       └── application-test.properties
│   │
│   └── test/
│       └── java/com/marlonb/game_leaderboard_api/
│           ├── controller/
│           │   ├── PlayerControllerSliceTests.java
│           │   └── UserControllerSliceTests.java
│           │
│           ├── service/
│           │   ├── PlayerServiceUnitTests.java
│           │   ├── UserDetailServiceUnitTests.java
│           │   ├── GameUserDetailServiceUnitTests.java
│           │   └── JwtServiceUnitTests.java
│           │
│           ├── repository/
│           │   ├── PlayerRepositoryIntegrationTests.java
│           │   └── UserControllerIntegrationTests.java
│           │
│           ├── security/
│           │   └── GameLeaderboardSecurityTests.java
│           │
│           ├── test_data/
│           │   ├── PlayerTestData.java
│           │   ├── Player2TestData.java
│           │   ├── Player3TestData.java
│           │   ├── Player4TestData.java
│           │   └── user/
│           │       ├── User1TestData.java
│           │       ├── User2TestData.java
│           │       └── AdminUser1TestData.java
│           │
│           ├── test_assertions/
│           │   ├── PlayerTestAssertions.java
│           │   └── UserTestAssertions.java
│           │
│           └── test_securityConfig/
│               └── TestSecurityConfig.java
│
└── pom.xml                           # Maven configuration
```

**Detailed structure available in:** [`project-structure.txt`](./../project-structure.txt)

---

## 7. Environment Configuration

| Profile    | Database | Port | Purpose                    |
|------------|----------|------|----------------------------|
| `dev`      | H2       | 8080 | Local development          |
| `test`     | H2       | 8080 | Automated testing          |
| `prod`     | MySQL    | 8080 | Production deployment      |

## 8. Database Configuration
```
DB_URL=jdbc:mysql://localhost:3306/game_leaderboard
DB_USERNAME=your_username
DB_PASSWORD=your_password
```
## JWT Configuration
```
JWT_SECRET=${jwt.secret-key}
JWT_EXPIRATION=${jwt.expiration-time}
```

## Base Admin Credentials
```
BASE_ADMIN_USERNAME=t430
BASE_ADMIN_PASSWORD=Dummy#07
```

---

## 8. CI/CD Pipeline

**GitHub Actions Workflow:**
1. **Checkout Stage**
    - Checkout code from repository
    - Uses: `actions/checkout@v3`

2. **Environment Setup**
    - Set up JDK 24 (Temurin distribution)
    - Configure Maven cache
    - Uses: `actions/setup-java@v3`

3. **Build Stage**
    - Clean and compile project with Maven
    - Command: `mvn clean compile`

4. **Test Stage**
    - Run unit tests with Maven Surefire
    - Command: `mvn test`
    - Continue execution even if tests fail (for complete test reporting)

5. **Test Reporting**
    - Generate Surefire test report
    - Command: `mvn surefire-report:report`
    - Executed regardless of test results

6. **Artifact Upload**
    - Upload test results to GitHub Actions
    - Artifacts: Surefire reports from `target/surefire-reports/`
    - Retention: Available for download after pipeline completion
    - Uses: `actions/upload-artifact@v4`

**Triggers:**
- Push to `main` branch
- Pull requests to `main` branch

**Artifacts Generated:**
- Test results (Surefire HTML and XML reports)
- Test execution logs

---

## 9. Testing Strategy

### Test Infrastructure
- **Test Data Builders:** Provide test data for each architectural layer
- **Test Security Config:** Mock authentication and authorization for controller tests
- **Test-Driven Development:** Applied for Controller and Security layers

### Unit Tests (JUnit 5 + Mockito)
- Service layer business logic
- Categorized with Positive and Negative Tests
- Repository layer data access
- **Target Coverage:** 80%+

### Slice Tests (JUnit 5 + MockMvc)
- Controller endpoints with MockMvc
- Categorized with Positive and Negative Tests
- Security testing with role-based access (USER, ADMIN)
- Request/response validation

### Security Integration Tests (SpringBootTest + RestTemplate)
- Full application context with real HTTP server (random port)
- Categorized with Positive and Negative Tests
- Real HTTP requests with `RestTemplate`, actual JWT validation, mocked service layer

### API Tests (Postman)
**Test Categories:**
1. **Authentication Tests**
    - Token generation
    - Login (positive & negative)
    - Registration (regular & admin, positive & negative)

2. **User Management Tests**
    - Self-endpoints (CRUD on own profile)
    - Admin-endpoints (CRUD on any user)
    - Negative scenarios (validation errors, unauthorized access)

3. **Player Management Tests**
    - Self-endpoints (CRUD on own player)
    - Admin-endpoints (CRUD on any player)
    - Negative scenarios (duplicate players, invalid data)

4. **Leaderboard Tests**
    - Top 3 players retrieval
    - Ranking accuracy

5. **Account Viewer Tests**
    - Get all players/users (admin only)
    - Get specific accounts

**Test Management (TestRail):**
- Test case documentation
- Test execution tracking
- Defect management
- Test coverage reports

**Automation:**
- Postman Newman for CI/CD integration
- Pre-request scripts for test data setup
- Automated cleanup after test execution

---

## 10. Error Handling

### Standard Error Response Format

#### Sample Format
```json
{
  "timestamp": "2025-10-11T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "user": [
        "Invalid username or password!"
    ]
  },
  "path": "/api/players"
}
```

--- 

## 11. Future Improvements

### Features
- Pagination for player lists and leaderboard
- Filtering and sorting options
- Player statistics and history
- Real-time leaderboard updates (WebSocket)
- Achievement system
- Multi-game support

### Technical Enhancements
- Redis caching for leaderboard
- Rate limiting
- API versioning
- Swagger/OpenAPI documentation
- Docker containerization
- Kubernetes deployment
- Monitoring and logging (ELK stack)
- Database indexing optimization
- Token refresh mechanism
- Email verification for registration
- Password reset functionality

### Testing
- Performance testing
- Load testing
- Security penetration testing