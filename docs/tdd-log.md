# TDD Log

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
- Chose MySQL for production, H2 for local/test

**🧠 Reflection:**  
✅ Clear project direction and design before jumping into code  
🧠 Helped solidify leaderboard logic and backend flow  
📌 **Next Step:** Create `Player` entity, write first test for leaderboard ranking logic

