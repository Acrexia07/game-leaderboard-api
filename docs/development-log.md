# Development Logs

---
## Table of Contents
- [Day 1: Project Setup and CI Integration](#day-1-project-setup-and-ci-integration)
- [Day 2: Project Planning & Architecture Design](#day-2-project-planning--architecture-design)
- [Day 3: Database Configuration & JWT Foundation](#day-3-database-configuration--jwt-foundation)
- [Day 4: Entity Development & TDD Implementation](#day-4-entity-development--tdd-implementation)
- [Day 5: Service Layer Expansion - Read Operations](#day-5-service-layer-expansion---read-operations)
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
---

*Development continues with focus on building resilient, well-tested service architecture*