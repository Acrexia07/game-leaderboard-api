# 🧱 Architecture Overview – Game Leaderboard API

---

## 1. Project Purpose
- Project Goal: Provide a REST API that manages a game leaderboard.
- Learning Goal: Strengthen Java fundamentals (Collections, Streams, Threading, etc.), apply Spring Boot with JWT-based security, 
and practice CI/CD using GitHub Actions.

## 2. Tech Stack
| Component       | Tech Used                                      |
|-----------------|------------------------------------------------|
| Language        | Java                                           |
| Framework       | Spring Boot                                    |
| Build Tool      | Maven                                          |
| Dependencies    | Spring Web, Spring Security, Lombok, MapStruct |
| Testing Tools   | JUnit 5, Mockito, Postman                      |
| CI/CD           | GitHub Actions (CI practice)                   |
| Database (Dev)  | H2                                             |
| Database (Prod) | MySQL                                          |
| Auth            | JWT                                            |

## 3. System Design
### 3.1. Data Model — Player
| Field       | Type          | Validation                           |
|-------------|---------------|--------------------------------------|
| `id`        | long          | Auto-generated (no need to validate) |
| `uuid`      | UUID          | Auto-generated                       |
| `name`      | String        | Not empty, max 10 characters         |
| `score`     | int           | Must be present                      |
| `timestamp` | LocalDateTime | Must not be in the future            |

### 3.2. API Endpoints
| Method | Path                | Description              |
|--------|---------------------|--------------------------|
| GET    | `/api/leaderboard`  | Get leaderboard rankings |
| GET    | `/api/players`      | List all players         |
| GET    | `/api/players/{id}` | Get a specific player    |
| POST   | `/api/players`      | Add a new player         |
| PUT    | `/api/players/{id}` | Update player details    |


## 4. Architecture Layers
Repository → Service → Controller

## 5. Authentication
JSON Web Token (JWT)
- Secure endpoints
- Basic token generation & verification

## 6. Environment 
| Profile    | Database |
|------------|----------|
| `dev/test` | H2       |
| `prod`     | MySQL    |

