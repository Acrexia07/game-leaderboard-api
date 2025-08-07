# Troubleshooting log

---
## Table of Contents
- [Issue 1 (July 31, 2025): Test configuration failure](#issue-1-july-31-2025-test-configuration-failure)
- [Issue 2 (August 7, 2025): Custom Query abstract method failure](#issue-2-august-7-2025-custom-query-abstract-method-failure)
---
## Technical issues encountered

### Issue 1 (July 31, 2025): Test configuration failure
- **🐞 Issue:** Auto-generated `contextLoads()` test failing due to incomplete configuration.
- **🧪 Solution:** Removed unnecessary test file: `GameLeaderboardApiApplicationTests.java`.
- **✅ Result:** CI pipeline restored to successful build status.

### Issue 2 (August 7, 2025): Custom Query abstract method failure
- **🐞 Issue:** Error occurred after adding custom query method in PlayerRepository `boolean existsByName (String name)`.
- **🧪 Solution:** update the variable declared in the parameter of the custom query method from `name` to `playerName` 
that matches the player entity attribute `playerName`.
- **✅ Result:** CI pipeline restored to successful build status.