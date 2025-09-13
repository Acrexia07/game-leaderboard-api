# Development Logs

---
## Table of Contents
- [Day 1: Project Setup and CI Integration](#day-1-project-setup-and-ci-integration)
- [Day 2: Project Planning & Architecture Design](#day-2-project-planning--architecture-design)
- [Day 3: Database Configuration & JWT Foundation](#day-3-database-configuration--jwt-foundation)
- [Day 4: Entity Development & TDD Implementation](#day-4-entity-development--tdd-implementation)
- [Day 5: Service Layer Expansion - Read Operations](#day-5-service-layer-expansion---read-operations)
- [Day 6: Test Case Development extension — Negative Test for create operation](#day-6-test-case-development-extension--negative-test-for-create-operation)
- [Day 7: Continuation of TDD development with other Service layer functions and some custom exception handling](#day-7-continuation-of-tdd-development-with-other-service-layer-functions-and-some-custom-exception-handling)
- [Day 8: Test Case Development extension (Negative Test) and Basic Auth Implementation](#day-8-test-case-development-extension-negative-test-and-basic-auth-implementation)
- [Day 9: Test Case Development continuation (Negative Test for update operation)](#day-9-test-case-development-continuation-negative-test-for-update-operation)
- [Day 10: Test Case Development continuation (Negative Test for update operation)](#day-10-test-case-development-continuation-negative-test-for-update-operation)
- [Day 11: Test Case Development for controller continuation](#day-11-test-case-development-for-controller-continuation)
- [Day 12: Negative test case development for Controller layer](#day-12-negative-test-case-development-for-controller-layer)
- [Day 13: Custom exception handler development intended for java date time entity attribute](#day-13-custom-exception-handler-development-intended-for-java-date-time-entity-attribute)
- [Day 14: Refactor all `PlayerService`-related tests](#day-14-refactor-all-playerservice-related-tests)
- [Day 15: Top 3 player repository implementation - Query and Test](#day-15-top-3-player-repository-implementation---query-and-test)
- [Day 16: Top 3 player controller and service implementation](#day-16-top-3-player-controller-and-service-implementation)
- [Day 17: WebSocket Learning Phase](#day-17-websocket-learning-phase)
- [Day 18: Refactor instantiation of `timestamp` attributes](#day-18-refactor-instantiation-of-timestamp-attributes)
- [Day 19: JWT Implementation Preparation](#day-19-jwt-implementation-preparation)
- [Day 20: JWT Implementation Preparation - DTOs creation](#day-20-jwt-implementation-preparation---dtos-creation)
- [Day 21: JWT Implementation preparation - `UserService` implementation](#day-21-jwt-implementation-preparation---userservice-implementation)
- [Day 22: Expand `UserService` functions with additional CRUD operations](#day-22-expand-userservice-functions-with-additional-crud-operations)
- [Day 23: TDD Continuation - Negative Test Case Development for `UserService`](#day-23-tdd-continuation---negative-test-case-development-for-userservice)
-[]

---

## Day 1: Project Setup and CI Integration
**📅 Date:** July 27, 2025

**🎯 Objectives:**
- Initialize Spring Boot project using Spring Initializr
- Set up GitHub repository and establish version control
- Configure GitHub Actions for automated CI pipeline
- Validate TDD setup with initial test implementation

**🛠️ Implementation Summary:**
- Created Spring Boot project with core dependencies: Web, Lombok, DevTools, Security (JWT planned)
- Initialized Git repository and pushed initial codebase to GitHub
- Configured GitHub Actions using Java CI workflow (`.github/workflows/ci.yml`)
- Implemented simple dummy test (`DummyTest.java`) to validate CI and JUnit integration

**🧠 Key Outcomes:**  
✅ **CI Pipeline Success:** Automated builds execute successfully on every push  
✅ **Test Infrastructure Validated:** Dummy test passed, confirming test runner and JUnit setup

📌 **Next Step:** Project design and architecture planning

---

## Day 2: Project Planning & Architecture Design
**📅 Date:** July 28, 2025

**🎯 Objectives:**
- Define comprehensive project architecture and scope
- Document project purpose, features, and technical specifications
- Design data model and authentication strategy
- Create architectural documentation

**🛠️ Implementation Summary:**
- Outlined project objectives and learning goals
- Defined core features: player scoring system, leaderboard ranking, JWT authentication
- Designed layered architecture pattern (Controller → Service → Repository)
- Created `Player` entity specification with field-level validation constraints
- Planned API endpoint structure and routing
- Selected database strategy: MySQL for production, H2 for testing

**🧠 Key Insights:**
- ✅ **Strategic Planning:** Established clear project direction before code implementation  
- 🧠 **Architecture Clarity:** Solidified leaderboard logic and backend data flow understanding

📌 **Next Step:** Database configuration with environment profiles & Spring Security JWT setup

---

## Day 3: Database Configuration & JWT Foundation
**📅 Date:** July 29, 2025

**🎯 Objectives:**
- Configure multi-environment database profiles (local/test)
- Set up Docker containerization for MySQL
- Establish JWT dependency foundation for future authentication

**🛠️ Implementation Summary:**
- Configured `application.properties` for local profile with MySQL integration
- Configured `application.properties` for test profile with H2 Console
- Implemented MySQL Docker container setup for development environment
- Added JWT dependencies to `pom.xml` for upcoming security implementation

**🧠 Strategic Value:**  
- ✅ **Security Foundation:** Established groundwork for Spring Security JWT integration  
- 🧠 **Environment Organization:** Streamlined configuration management before core development

📌 **Next Step:** TDD implementation with Player entity creation and test data development

---

## Day 4: Entity Development & TDD Implementation
**📅 Date:** July 31, 2025

**🎯 Objectives:**
- Implement Test-Driven Development approach for core functionality
- Create Player entity with comprehensive validation
- Develop service layer function for player data persistence

**🛠️ Implementation Summary:**
- Created `Player` entity with corresponding DTOs and validation constraints
- Developed test data helper methods and comprehensive test cases
- Implemented `savePlayerData` service function with full test coverage

**🐞 Technical Challenge Resolved:**
- **Issue:** Auto-generated `contextLoads()` test failing due to incomplete configuration
- **Solution:** Removed unnecessary test file: `GameLeaderboardApiApplicationTests.java`
- **Result:** CI pipeline restored to successful build status

**🧠 Development Insights:**  
✅ **Real-time Debugging:** UUID behavior observation provided immediate feedback for issue resolution  
🧪 **TDD Value Recognition:** Service layer development driven by tests enhanced design appreciation

📌 **Next Step:** Expand service layer with additional CRUD operations

---

## Day 5: Service Layer Expansion - Read Operations
**📅 Date:** August 1, 2025

**🎯 Objectives:**
- Extend TDD approach to data retrieval operations
- Implement service layer function for comprehensive player data access
- Maintain CI/CD pipeline integrity with new test implementations

### **🛠️ Implementation Summary:**

### Test Development
- Created comprehensive test cases for player data retrieval using TDD methodology
- Implemented corresponding service layer functions based on test-driven requirements
- Successfully maintained GitHub Actions CI pipeline with all tests passing

### Technical Implementation
**Approach:** Stream-based data processing for optimal performance and code clarity

**Core Logic Flow:**
1. **Data Retrieval:** Instantiated `PlayerEntity` objects via `playerRepository.findAll()`
2. **Data Transformation:** Applied stream mapping using `playerMapper.toResponse()` method
3. **Collection Aggregation:** Collected transformed data using `toList()` collector

```java
// Implementation pattern
public List<PlayerResponseDto> retrieveAllPlayersData() {

    List<PlayerEntity> listOfPlayers = playerRepository.findAll();
    
    return playerRepository.findAll()
        .stream()
        .map(playerMapper::toResponse)
        .toList();
}
```

**🧠 Technical Learning:**  
- ✅ **Stream Processing Benefits:** Significant reduction in data-processing boilerplate code  
- ✅ **Enhanced Debugging Skills:** Improved test creation capabilities through systematic debugging approach  
- 🔍 **Test Behavior Analysis:** Gained deeper insights into player data behavior within test environments

📌 **Next Step:** Test Case Development extension — Negative Test for create operation

---
## Day 6: Test Case Development extension — Negative Test for create operation
**📅 Date:** August 2, 2025

**🎯 Objectives:**
- Develop Negative test case for create operations
- Maintain CI/CD pipeline integrity with new test implementations

**🛠️ Implementation Summary:**
- Created comprehensive negative test cases for player data creation
- Successfully maintained GitHub Actions CI pipeline with all tests passing

**🧠 Technical Learning:**
- **💭 Concept Recall:** Able to recall concepts in negative with Parameterized Test implementation

📌 **Next Step:** Continuation of TDD development with other Service layer functions

---

## Day 7: Continuation of TDD development with other Service layer functions and some custom exception handling
**📅 Date:** August 4, 2025

**🎯 Objectives:**
- Develop positive test cases for the other service layer functions.
- Implement service-layer functions based on test cases.
- Develop initial custom exception handler with `ErrorResponseDto` and `GlobalExceptionHandler`.

**🛠️ Implementation Summary:**
- Created comprehensive service function implementing test-driven development approach.
- Updated test data based on the requirements needed based on the developed test cases.
- Developed global exception handlers and some custom exceptions for preparation.

**🧠 Technical Learning:**
- **💭 Concept Recall:** Able to recall concepts about custom exception handler development.
- **💡 Shift mindset:** Able to implement other way of displaying error messages (field errors) with 
Map implementation.

📌 **Next Step:** Test Case Development extension (Negative Test) and Basic Auth Implementation

---

## Day 8: Test Case Development extension (Negative Test) and Basic Auth Implementation
**📅 Date:** August 6, 2025

**🎯 Objectives:**
- Develop negative test cases for other service functions.
- Implement basic authentication for spring security (for now).

**🛠️ Implementation Summary:**
- Developed negative test case for read by player's `id` operation.
- Implemented basic authentication for initial api endpoint testing later on.
- Developed ApiResponseDto in preparation with controller layer functions development.

**🧠 Technical Learning:**
- **💭 Concept Recall:** Able to recall some syntax that I used for creating negative test cases with the read scenario.
Also, I was able to recall on how to configure basic authentication with spring security.

📌 **Next Step:** Test Case Development continuation (Negative Test for update operation)

---

## Day 9: Test Case Development continuation (Negative Test for update operation)
**📅 Date:** August 7, 2025

**🎯 Objectives:**
- Develop negative test cases for other service functions (update operation).
- Implement additional logic to check duplication on `playerName` in create and update operation.

**🛠️ Implementation Summary:**
- Developed negative test case for update by player's `id` operation.
- Implemented additional logic to check duplication on `playerName` in create and update operation based on test case.
- Added new custom exception for resource duplication `DuplicateResourceFoundException`.

**🐞 Technical Challenge Resolved:**
- **🐞 Issue:** Error occurred after adding custom query method in PlayerRepository `boolean existsByName (String name)`.
- **🧪 Solution:** update the variable declared in the parameter of the custom query method from `name` to `playerName`.
  that matches the player entity attribute `playerName`.
- **✅ Result:** CI pipeline restored to successful build status.

**🧠 Technical Learning:**
- **🤯 Repositories' Custom Query Method Realization:** 
I was able to understand the implementation of custom query abstract method in the repository wherein
the parameter that is used to be declared in the query method must be an entity attribute.
- **🧪 TDD Value Recognition:** Service layer development driven by tests enhanced design appreciation

📌 **Next Step:** Test Case Development for controller

---

## Day 10: Test Case Development continuation (Negative Test for update operation)
**📅 Date:** August 9, 2025

**🎯 Objectives:**
- Develop test case for preparation to implement controller-layer function for retrieve all player data (GET).
- Implement controller-layer function for retrieve all player data.

**🛠️ Implementation Summary:**
- Developed test case for controller-layer function that retrieves all players data (GET).
- Implement controller-layer function that retrieves all player data.
- Refactor general error message of exception by implementing ENUMS.
- Fixed issue about UUID generation when creating a player resource.

**🐞 Technical Challenge Resolved:**
- **🐞 Issue:** Could not execute statement due to `uuid` is null.
- **Cause:** UUID was not being generated because it wasn’t set in the entity.
- **🧪 Solution:** Implement `@PrePersist` method in the `PlayerEntity` so it’s always generate UUID if null.

**🧠 Technical Learning:**
- **🤯 UUID generation realization:**
I was able to be aware of the usage of `PrePersist` which is helpful when I need to randomly generate a UUID.
- **🧪 TDD Value Recognition:** I was able to recall firmly the logic implementation of controller unit testing.

📌 **Next Step:** Test Case Development for controller continuation

---

## Day 11: Test Case Development for controller continuation
**📅 Date:** August 10, 2025

**🎯 Objectives:**
- Developed test case for controller-layer function that retrieves specific players data (GET).
- Developed test case for controller-layer function that updates specific players data (PUT).
- Developed test case for controller-layer function that deletes specific players data (DELETE).

**🛠️ Implementation Summary:**
- Implements test cases in every crud operations for controller-layer functions.
- Develop other controller-layer functions that pass the test cases created.

**🧠 Technical Learning:**
- **🧪 Unit Testing Recall:** I was able to recall firmly the logic implementation of controller unit testing.

📌 **Next Step:** Negative test case development for Controller layer

---

## Day 12: Negative test case development for Controller layer
**📅 Date:** August 11, 2025

**🎯 Objectives:**
- Develop negative test case for `player name` duplication on save operations.
- Develop negative test case for missing or null input(s).
- Develop negative test case for player's `id` that does not exist.

**🛠️ Implementation Summary:**
- Implemented negative test case for `player name` duplication on save operations.
- Implemented negative test case for missing or null input(s).
- Implemented negative test case for player's `id` that does not exist.

**🧠 Technical Learning:**
- **🧪 Unit Negative Testing Recall:** I was able to recall firmly the logic implementation of controller unit negative 
testing.

📌 **Next Step:** Custom exception handler development intended for java date time entity attribute

---

## Day 13: Custom exception handler development intended for java date time entity attribute
**📅 Date:** August 12, 2025

**🎯 Objectives:**
- Create a custom exception handlers for java date time entity attribute.
- create the negative test case that catches those custom exceptions.

**🛠️ Implementation Summary:**
- Developed a custom exception handling for checking JSON especially `timestamp` attribute using 
`HttpMessageNotReadableException` class.
- Implement negative testing that captures invalid timestamp formats.

**🐞 Technical Challenge Resolved:**

- **Issue 1: DateTime format validation not working in JSON requests**
  - **🐞 Issue:** Expected `@ExceptionHandler(DateTimeParseException.class)` to catch invalid datetime formats in JSON
      requests, but handler wasn't triggering.
  - **Cause:** JSON parsing happens at Jackson level first. `HttpMessageNotReadableException` wraps all JSON deserialization errors before they reach `DateTimeParseException`.****
  - **🧪 Solution:** Use `HttpMessageNotReadableException` handler with message inspection to customize error responses

- Issue 2: Parameterize test for controller negative testing not working
  - **🐞 Issue:** Invalid timestamp used are not well tested with parameterized tests.
  - **Cause:** In parameterized testing in controller layer for invalid formats, updating DTOs with invalid date strings
    is not possible because it requires parsing.
  - **🧪 Solution:** Instantiate an `ObjectMapper` via `createObjectNode()` method and execute `put()`

**🧠 Technical Learning:**
- **📝 Lesson Learned:** 
  - Always check the exception hierarchy when dealing with JSON request validation.
    DateTimeParseException only catches direct Java datetime parsing, not JSON deserialization errors.
  - If there's a parsing needed in an attribute when testing, instead of updating DTOs, instantiate
    an ObjectMapper.

📌 **Next Step:** Refactor all tests

---

## Day 14: Refactor all `PlayerService`-related tests
**📅 Date:** August 14, 2025

**🎯 Objectives:**
- Refactor all the tests.

**🛠️ Implementation Summary:**
- Restructuring all of test

📌 **Next Step:** Top 3 player repository implementation - Query and Test

---

## Day 15: Top 3 player repository implementation - Query and Test
**📅 Date:** August 15, 2025

**🎯 Objectives:**
- Develop custom query method to retrieve top 3 players in terms of their scores.
- Implement test case that check if mapped top players return successfully when the repository returns entities.

**🛠️ Implementation Summary:**
- Created custom query method that retrieves top 3 player as required by the objectives:
  ```java
  @Query(value = "SELECT * FROM player_data p ORDER BY p.scores DESC, p.timestamp ASC LIMIT 3",
         nativeQuery = true)
  List<PlayerEntity> findTop3PlayerByOrderByScoresDescAndTimestampAsc ();
- Implemented that required positive test case based on the objective.

**🐞 Technical Challenge Resolved:**

- **Custom Query abstract method failure with the exception `UnsatisfiedDependencyException`**
  - **🐞 Issue:** Error occurred after adding custom query method in PlayerRepository
    ```java   
    @Query("SELECT p FROM player_data p ORDER BY p.scores DESC LIMIT 3, p.timestamp ASC LIMIT 3")
    List<PlayerEntity> findTop3PlayerByOrderByScoresDescAndTimestampAsc ();
  - **Cause:** Query value is not recognized as native SQL
  - **🧪 Solution:** Add `nativeQuery = true` inside the query annotation just like this
    ```java
    @Query(value = "SELECT * FROM player_data p ORDER BY p.scores DESC LIMIT 3, p.timestamp ASC LIMIT 3",
           nativeQuery = true)
    List<PlayerEntity> findTop3PlayerByOrderByScoresDescAndTimestampAsc ();
  - **✅ Result:** No issue related to this occurred again.

**🧠 Technical Learning:**
- **🧘‍♂️ Self reflection:** 
  - I was able to understand the importance of activating the `nativeSQL` when developing custom query method in 
  repository. 
  - I was able to understand the test strategy on implementing test for retrieving top 3 players data in implementing 
  leaderboard logic.

📌 **Next Step:** Controller function for leaderboard

---

## Day 16: Top 3 player controller and service implementation
**📅 Date:** August 18, 2025

**🎯 Objectives:**
- Develop test case that should pass when retrieve top 3 players.
- Implement service function based on the test case developed.
- Develop test case that return appropriate response entity after executing service function under `GET` mapping.
- Implement controller function based on the test case developed.

**🛠️ Implementation Summary:**
- Developed test case that checks the retrieval of top 3 players.
- Implemented service function via `Streams` that executes repository query method and mapped with `toResponse()` method of 
the `playerMapper`.
- Developed test case that returns appropriate response entity after executing service function under `GET` mapping.
- Implemented controller function based on the developed test case.

**🧠 Technical Learning:**
- **🧘‍♂️ Controller Unit test familiarization:** I was able to familiar further more about controller 
unit test case development.

📌 **Next Step:** WebSocket Learning Phase

---

## Day 17: WebSocket Learning Phase
**📅 Date:** August 19, 2025

**🎯 Objectives:**
- Know the overview of websocket
- Apply it by creating the simple app

**🧠 Technical Learning:**
- **📖 New Knowledge Acquired: ** I was able to learn the usage and implementation of websocket theoretically.

📌 **Next Step:** Refactor instantiation of `timestamp` attributes

---

## Day 18: Refactor instantiation of `timestamp` attributes
**📅 Date:** August 19, 2025

**🎯 Objectives:**
- Implement `timestamp` attribute annotated with `@PrePersist` and `@PreUpdate` on entity.
- Remove `timestamp` attribute on request and update DTOs.
- Refactor all classes affected by the `timestamp` implementation changes.

**🛠️ Implementation Summary:**
- Implemented `timestamp` attribute annotated with `@PrePersist` and `@PreUpdate` on entity.
- Removed `timestamp` attribute on request and update DTOs.
- Refactored all classes affected by the `timestamp` implementation changes.

📌 **Next Step:** Implementation of JWT - Learning phase

**🎯 Objectives:**
- Learn deeper about spring security.
- Plan on how to implement JWT on this project.

_"Note: The project will be halted for more days to study the WebSocket concept"_

_"After learning Websocket concept theoretically, I conclude that I need to implement JWT first in the project"_

---

## Day 19: JWT Implementation Preparation
**📅 Date:** August 23, 2025

**🎯 Objectives:**
- Create a `UserEntity` with provided attributes.
- Create a `UserRepository` that extends `JpaRepository`.

**🛠️ Implementation Summary:**
- Implemented a structured entity in preparation for JWT implementation.
  ```java
  @Entity
  @Table(name = "users_data")
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public class UserEntity {
  
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
  
      @Column(unique = true, nullable = false)
      private String username;
  
      @Column(nullable = false)
      private String password;
  
      @Enumerated(EnumType.STRING)
      private UserRoles role = UserRoles.PLAYER;
  
      private LocalDateTime createdAt;
  
      @PrePersist
      public void onCreate () {
          this.createdAt = LocalDateTime.now();
      }
  }

- Implemented `UserRepository` that extends `JpaRepository`.

📌 **Next Step:** JWT Implementation preparation - DTOs creation

---

## Day 20: JWT Implementation Preparation - DTOs creation
**📅 Date:** August 23, 2025

**🎯 Objectives:**
- Create data transfer objects for `userEntity`.
- Provide sample test data based on the developed DTOs.
- Implement mapper that will map DTOs as per request.

**🛠️ Implementation Summary:**
- Created data transfer objects for `userEntity`.
- Provided sample test data based on the developed DTOs.
- Implemented mapper that will map DTOs as per request.

**🐞 Technical Challenge Resolved:**
- **🐞 Issue:** Error creating bean with name `userRepository`.
- **Cause:** Having Optional on the custom query method in the `userRepository` class.
  ```
  @Repository
  public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername (String username);
  }

- **🧪 Solution:** Removed `Optional` defined in the custom query method.
  ```
  @Repository
  public interface UserRepository extends JpaRepository<UserEntity, Long> {
  UserEntity findByUsername (String username);
  }
- **✅ Result:** No issue related to this occurred again.

📌 **Next Step:** JWT Implementation preparation - `UserService` implementation

## Day 21: JWT Implementation preparation - `UserService` implementation
**📅 Date:** August 25, 2025

**🎯 Objectives:**
- Implement Test-Driven Development approach for core functionality
- Create `UserEntity` with comprehensive validation for user management
- Develop service layer function for user data persistence

**🛠️ Implementation Summary:**
- Created `UserEntity` with corresponding DTOs and validation constraints
- Developed test data helper methods and comprehensive test cases
- Implemented `createUser` service function with full test coverage

📌 **Next Step:** Expand service layer functions with additional CRUD operations

---

## Day 22: Expand `UserService` functions with additional CRUD operations
**📅 Date:** August 26, 2025

**🎯 Objectives:**
- Implement Test-Driven Development approach on other service functions.
- Develop other service layer functions for user data persistence.

**🛠️ Implementation Summary:**
- Implemented Test-Driven Development approach on other service functions and has no issues on GitHub Actions CI.
- Developed other service layer functions for user data persistence.

**🧠 Technical Learning:**
- **📖 New Knowledge Acquired: ** I was able to learn that by using `@RequiredArgsConstructor`, I was able to reduce 
boilerplate in implementing constructor-based dependency injection.

📌 **Next Step:** TDD Continuation - Negative Test Case Development for `UserService`

---

## Day 23: TDD Continuation - Negative Test Case Development for `UserService`
**📅 Date:** August 27, 2025

**🎯 Objectives:**
- Implement Test-Driven Development approach on other service functions with negative testing.
- Develop implementation logic that satisfies negative testing.

**🛠️ Implementation Summary:**
- Implemented Test-Driven Development approach on other service functions with negative testing.
- Developed implementation logic that satisfies negative testing.
- Refactored service error messages by inserting them all in an enumerated class.
- Implemented `@Transactional` on each user service functions for rollback in case of issues.

**🧠 Technical Learning:**
- **📖 Knowledge Recall: ** I was able to recall all the procedures when developing a negative test case.

📌 **Next Step:** TDD Continuation - Positive test case development of the controller layer functions

---

## Day 24: TDD Continuation - Positive test case development of the controller layer functions
**📅 Date:** August 28, 2025

**🎯 Objectives:**
- Implement Test-Driven Development approach on controller layer functions with positive testing.
- Develop implementation logic that satisfies the test created.

**🛠️ Implementation Summary:**
- Implemented Test-Driven Development approach on controller layer functions of registration of user account with positive testing.
- Developed implementation logic that satisfies the test created.

**🧠 Technical Learning:**
- **📖 Knowledge Recall: ** I was able to recall all the procedures when developing a positive test case on
controller layer functions.

📌 **Next Step:** Security Planning – JWT & Endpoint Authorization Design

---

## Day 25: Security Planning – JWT & Endpoint Authorization Design
📅 Date: August 29, 2025

**🎯 Objectives:**
- Focus solely on planning and documentation (no coding today).
- Define how JWT authentication will be integrated into the project.
- Establish endpoint authorization rules (Public, Authenticated User, Admin).
- Clarify the distinction between UserEntity (security accounts) and potential GameAccountEntity (game feature).

**🛠️ Implementation Summary:**
- No implementation today; only brainstorming and planning.
- Documented endpoint security mapping:
    ```
    POST /api/users/register → Public
    GET /api/users/{id}, PUT /api/users/{id}, DELETE /api/users/{id} → Authenticated User (self)
    GET /api/users → Admin only

- Outlined JWT flow: registration → login → token issuance → access protected endpoints.

**🧠 Technical Learning:**
- Recognized that planning is as important as coding for scalable security design.
- Identified separation of concerns: security users vs in-game player accounts.

📌 Next Step: JWT preparation - Continuation of implementing controller layer function for retrieve all user data.

---

## Day 26: TDD Continuation - Positive test case development of the other controller layer functions
**📅 Date:** August 30, 2025

**🎯 Objectives:**
- Implement Test-Driven Development approach on the other controller layer functions with positive testing.
- Develop implementation logic that satisfies the test created.

**🛠️ Implementation Summary:**
- Implemented Test-Driven Development approach on controller layer functions that retrieve all user data through admin permissions only
with positive testing.
- Developed implementation logic that satisfies the test created.

**🧠 Technical Learning:**
- **📖 Knowledge Recall: ** I was able to recall all the procedures when developing a positive test case on
controller layer functions.

📌 **Next Step:** JWT Implementation Preparation - Test-Driven Development of Controller layer via Positive Testing

---

## Day 27: JWT Implementation Preparation - Test-Driven Development of Controller layer via Positive Testing
**📅 Date:** August 31, 2025

**🎯 Objectives:**
- Implement test case development with positive testing to be used as anchor when developing the controller layer functions.
- Develop controller layer functions based on the created test cases.

**🛠️ Implementation Summary:**
- Implemented test case development to be used as anchor when developing the controller layer functions.
- Developed controller layer functions based on the created test cases.

📌 **Next Step:** JWT Implementation Preparation - Test-Driven Development continuation of Controller layer 
via Negative Testing

---

## Day 28: JWT Implementation Preparation - Test-Driven Development of Controller layer via Positive Testing
**📅 Date:** September 1, 2025

**🎯 Objectives:**
- Implement test case development with negative testing for implemented controller layer functions.

**🛠️ Implementation Summary:**
- Implemented test case development with negative testing for implemented controller layer functions.
- Refactor all controller unit test after test case development.

📌 **Next Step:** JWT Implementation Preparation - Implementing Admin user roles in the `UserEntity`

---

## Day 29: JWT Implementation Preparation - Implementing Admin user roles in the `UserEntity`
**📅 Date:** September 2, 2025

**🎯 Objectives:**
- Develop Data Transfer objects for handling admin requests and response
- Create test data in preparation for test-driven development of admin endpoints.

**🛠️ Implementation Summary:**
- Develop Data Transfer objects for handling admin requests and response
- Create test data in preparation for test-driven development of admin endpoints.
- Implemented other abstract methods via overloading methods that will handle mapping of admin dto with `UserMapper`.

**🧠 Technical Learning:**
- **📖 Realization: ** I was able to learn and understand the consideration of having the admin user roles as part of
the user management for creation and implementation.

📌 **Next Step:** JWT Implementation Preparation - Admin Service Layer and Controller layer Implementation

---

## Day 30: JWT Implementation Preparation - Admin Service Layer and Controller layer Implementation
**📅 Date:** September 3, 2025

**🎯 Objectives:**
- Develop service layer functions for admin using Test-driven Development approach.
- Develop controller layer functions for admin using Test-driven Development approach.

**🛠️ Implementation Summary:**
- Developed service layer functions for admin using Test-driven Development approach.
- Developed controller layer functions for admin using Test-driven Development approach.
- Created negative test case on admin service layer function that checks username duplication.

**🧠 Technical Learning:**
- **📖 Realization: ** I was able to understand the importance of user management in spring security aspect.

📌 **Next Step:** Restructuring classes in preparation for JWT Implementation 

---

## Day 31: JWT Implementation Preparation - User Management Service & Security Testing Setup
**📅 Date:** September 5, 2025

**🎯 Objectives:**
- Implement classes that configure the usage of `users_data` database table.
- Restructure test classes affected by the new security configuration.
- Prepare foundation for upcoming JWT implementation.
- 
**🛠️ Implementation Summary:**
- Implemented `GameUserDetailsService` and related classes for database user management.
- Restructured security configuration classes affected by the new user management setup.
- Refactored test classes to work with Spring Security's `@WebMvcTest` limitations
- Created separate `TestSecurityConfig.java` for reusable in-memory security configuration in tests.

**🔄 Issues Encountered & Resolved:**
- Fixed 403 Forbidden errors in tests due to security filter chain rule ordering
- Resolved missing `SecurityFilterChain` bean in test context

**🧠 Technical Learning:**
- **📖 Key Realization:** Understanding the distinct usage of Spring profiles (local vs test) when creating separate 
test configurations.
- **⚡ Best Practice:** Separating test security configurations improves reusability across multiple test classes.

📌 **Next Step:** JWT Implementation Preparation

---

## Day 32: Authentication System Testing (Pre-JWT)
**📅 Date:** September 7, 2025

**🎯 Objectives:**
- Test program's security authentication using API testing (Postman).
- Fix issues encountered during testing.

**🛠️ Implementation Summary:**
- Tested program's security authentication using API testing (Postman).
- Fixed issues encounter during testing.

**🔄 Issues Encountered & Resolved:**
- Fixed issues about having Admin users getting 401 Unauthorized despite correct credentials.
- Resolved missing `final` keyword on the injected `UserRepository` dependency.

**📊 Testing Results:**
- ✅ Admin user creation: Working (201 Created)
- ✅ Admin authentication: Fixed (was 401, now 200)
- ✅ Role-based authorization: Functioning

**🧠 Technical Learning:**
- **Knowledge Reinforcement:** Remembered the importance of using `final` keyword with `@RequiredArgsConstructor` 
for proper dependency injection.

📌 **Next Step:** JWT Implementation Preparation - login dummy endpoint creation

---

## Day 33: JWT Implementation Preparation - login dummy endpoint creation
**📅 Date:** September 8, 2025

**🎯 Objectives:**
- Implement a dummy login endpoint and conduct API testing to verify communication.
- Implement service function that verifies credential during the controller execution.

**🛠️ Implementation Summary:**
- Added corresponding data transfer object in preparation for JWT implementation.
- Added login dummy endpoint for jwt preparation.
- Added service function that verifies user credentials and generate token (implemented in hardcoded string for now).
- Added global exception handler for handling bad credential exceptions.

**🧠 Technical Learning:**
- **💡 Gain Insight:** I was able to gain knowledge about preparation before implementing JWT in the project.

📌 **Next Step:** JWT Implementation 

---

## Day 34: JWT Implementation  - Implementation of JWT token generation
**📅 Date:** September 9, 2025

**🎯 Objectives:**
- Implement `JwtService` that handles jwt token generation.
- Understand the implementation of key generation.

**⚠️ Challenges Encountered:**
- Initially got only 156 bits instead of required 256 bits
- Understanding the relationship between key generation and JWT signing
- Testing JWT implementation from scratch proved complexity

**🛠️ Implementation Summary:**
- Implement `JwtService` that handles jwt token generation.
- Understood the concept of key generation by considering conversion of strings into bytes and achieving the required
amount of bits based on the algorithm to be executed (like HmacSHA256 algorithm that requires at least 256 bits).

**🧠 Technical Learning:**
- **💡 Realization:** I realized that implementation of JWT in the project is complex to understand, especially when it's
your first time.
  I've decided to document all the works that I've done up to generating JWT token.

📌 **Next Step:** JWT Implementation - User Login Testing

---

## Day 35: JWT Implementation - User Login Testing 
**📅 Date:** September 10, 2025

**🎯 Objectives:**
- Implement test cases (positive and negative) on User Login.

**🛠️ Implementation Summary:**
- Implemented test cases for user login

📌 **Next Step:** JWT Implementation - JWT-related service unit Testing

---

## Day 35: JWT Implementation - JWT-related service unit Testing
**📅 Date:** September 11, 2025

**🎯 Objectives:**
- Implement test cases on jwt-related service functions.

**🛠️ Implementation Summary:**
- Implemented test cases on the service function that verifies user credentials.
- Implemented test cases on `JwtService`.
- Implemented test cases on `GameUserDetailsService`.

**🧠 Technical Learning:**
- **💡 Realization:** I realize that the test implementation strategy on CRUD and authentication are different 
in each other. 
  I need to create my own test pattern in the authentication implementation.

📌 **Next Step:** JWT Implementation continuation - Token Validation

---

## Day 36: JWT Implementation Continuation – Token Validation
📅 Date: September 12, 2025

**🎯 Objectives:**
- Finalize JWT token validation in the security layer.

**🛠️ Implementation Summary:**
- Implemented token validation inside the security filter chain.
- Ensured incoming requests are processed only if the JWT token is valid and not expired.
- Verified integration flow between `JwtService` and `GameUserDetailsService`.

🧠 Technical Learning:
- **💡 Realization:** Token validation logic is the backbone of JWT-based security.
  Any misalignment (e.g., expired tokens not being caught) could expose endpoints.
- **⚡ Key Insight:** Separating concerns between token generation and validation leads to cleaner, testable code.

📌 Next Step: Clean up outdated tests and refine authorization rules.

---

## Day 37: Security Refinement – PreAuthorize & Test Cleanup
📅 Date: September 13, 2025

**🎯 Objectives:**
Introduce method-level authorization using @PreAuthorize.
Remove outdated controller unit tests that were designed for Basic Auth.

🛠️ Implementation Summary:
- Applied @PreAuthorize annotations in controller endpoints to enforce role-based access policies.
- Removed controller unit tests tied to Basic Auth since they no longer aligned with JWT flow.
- Documented rationale for test removal in commit messages for transparency.

**🧠 Technical Learning:**
- **⚡ Lesson Learned:** It’s okay to remove outdated tests if they hinder progress and what matters is planning proper
  replacement tests must align with the current architecture.
- **💡 Insight:** Declarative security annotations (@PreAuthorize) make intent clearer than centralized configuration alone.

📌 Next Step: Rebuild controller unit tests aligned with JWT authentication and @PreAuthorize authorization.

---

*Development continues with focus on building resilient, well-tested service architecture*