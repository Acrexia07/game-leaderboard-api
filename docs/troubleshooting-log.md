# Troubleshooting log

---
## Table of Contents
- [Issue 1 (July 31, 2025): Test configuration failure](#issue-1-july-31-2025-test-configuration-failure)
- [Issue 2 (August 7, 2025): Custom Query abstract method failure](#issue-2-august-7-2025-custom-query-abstract-method-failure)
- [Issue 3 (August 8, 2025): Failed to load ApplicationContext for WebMergedContextConfiguration](#issue-3-august-8-2025-failed-to-load-applicationcontext-for-webmergedcontextconfiguration)
- [Issue 4 (August 9, 2025): Exception related to the Java * Date Time occurred](#issue-4-august-8-2025-exception-related-to-the-java--date-time-occurred)
- [Issue 5 (August 10, 2025): Create resource issue due to UUID being null](#issue-5-august-9-2025-uuid-null-when-requesting-create-resource)

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

### Issue 3 (August 8, 2025): Failed to load ApplicationContext for WebMergedContextConfiguration
- **🐞 Issue:** Failed to load ApplicationContext for WebMergedContextConfiguration.
- **🧪 Solution:** Changed the annotation of the playerService declared from `@Autowired` to `@MockitoBean`.
- **✅ Result:** No issue related to this occurred again.

### Issue 4 (August 8, 2025): Exception related to the Java * Date Time occurred
- **🐞 Issue:** Unchecked exception occurred `InvalidDefinitionException` when serializing/deserializing Java 8+ time 
objects
- **🧪 Solution:**

    Added Jackson JSR310 module dependency for Java 8+ time support:
    ```xml
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
    </dependency>
    ```
  
    Configured ObjectMapper to handle Java time objects properly:

      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule()); // Enables Java 8+ time support
      mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS); // Use ISO format instead of timestamps

- **✅ Result:** No issue related to this occurred.

### Issue 5 (August 9, 2025): Create resource issue due to UUID being null
- **🐞 Issue:** Could not execute statement due to `uuid` is null.
- **Cause:** UUID was not being generated because it wasn’t set in the entity or service.
  - **🧪 Solution:** Generate UUID in the entity’s `@PrePersist` method so it’s always created before insert.
    ````java
    @PrePersist
    public void generateUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
    ````
  - **✅ Result:** No issue related to this occurred again.
    ````json
    {
      "apiMessage": "Player created successfully!",
      "Response": {
          "id": 1,
          "uuid": "3f46fcb3-3abc-4623-af0a-af51b8a24a39",
          "username": null,
          "scores": 1000,
          "timestamp": "2025-08-08T14:00:00"
      }
    }
    ````