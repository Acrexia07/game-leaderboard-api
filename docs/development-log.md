# Development Logs

---

## Day 1: Project Setup and CI Integration
**📅 Date:** July 27, 2025

**🎯 Goals:**
- Initialize Spring Boot project using Spring Initializr
- Set up GitHub repository and push the initial codebase
- Configure GitHub Actions for CI pipeline
- Write and run the first dummy test to verify TDD setup

**🛠️ What I Did:**
- Created Spring Boot project with dependencies: Web, Lombok, DevTools, Security (JWT later)
- Initialized Git repo and pushed to GitHub
- Configured GitHub Actions using Java CI workflow (`.github/workflows/ci.yml`)
- Wrote a simple dummy test (`DummyTest.java`) to validate CI and JUnit setup

**🧠 Reflection:**  
✅ CI successfully runs on every push  

✅ Dummy test passed — confirms test runner and JUnit setup  

📌 **Next Step:** Start project design and architecture planning

---

## Day 2: Project Planning + Architecture
**📅 Date:** July 28, 2025

**🎯 Goals:**
- Define and document project architecture
- Clarify purpose, features, and tech stack
- Design data model and authentication plan
- Write `docs/architecture.md`

**🛠️ What I Did:**
- Outlined project and learning objectives
- Listed core features: player scoring, leaderboard ranking, JWT auth
- Planned layered architecture (Controller → Service → Repository)
- Designed `Player` entity with field-level validations
- Sketched API endpoints
- Chose MySQL for local, H2 for test

**🧠 Reflection:**
- ✅ Clear project direction and design before jumping into code
- 🧠 Helped solidify leaderboard logic and backend flow

📌 **Next Step:** Configure Databases based on Profiles & Spring Security JWT Authentication


## Day 3: Configure Databases based on Profiles & JWT Initial Setup

**📅 Date:** July 29, 2025

**🎯 Goals:**
- Configure application.properties for local profile with MySQL
- Configure application.properties for test profile with H2 Console
- Set up Docker containerization for MYSQL
- Add JWT dependencies in the pom.xml

**🛠️ What I Did:**
- Configured application.properties for both local and test profiles
- Set up MySQL in Docker container
- Added JWT dependencies in pom.xml for upcoming JWT implementation

**🧠 Reflection:**  
- ✅ Laid the groundwork for integrating Spring Security with JWT
- 🧠 Helped me organize configurations before jumping into development

📌 **Next Step:** Test case and test data development, and creation of the 'Player' entity

## Day 3: Test case and test data development, and creation of the 'Player' entity
Date: July 31, 2025

**🎯 Goals:**
- Develop test cases and test data in TDD approach
- Create the `Player` entity and DTO with define validation constraints
- Implement a service-layer function to add player info

### 🛠️ What I Did:
- Created `Player` entity and corresponding DTOs
- Wrote test data helper methods and initial test case
- Implemented `savePlayerData` service function

### 🐞 Issue Encountered:
- **Error:**  Auto-generated `contextLoads()` test was failing due to incomplete config
- **Solution:** Deleted the unnecessary test file: `GameLeaderboardApiApplicationTests.java`
- **Result:** CI builds successfully without failing test artifacts

**🧠 Reflection:**  
- ✅ I was impressed by the debugging experience — seeing UUID changes in real-time helped me quickly identify and fix 
the issue
- 🧪 Working on the service layer using a TDD mindset helped me appreciate the value of tests in driving the design
📌 **Next Step:** Continue implementing and testing other functions in the service layer