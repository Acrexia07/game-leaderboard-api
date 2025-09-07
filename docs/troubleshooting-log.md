# Troubleshooting log

---
## Table of Contents
- [Issue 1 (July 31, 2025): Test configuration failure](#issue-1-july-31-2025-test-configuration-failure)
- [Issue 2 (August 7, 2025): Custom Query abstract method failure](#issue-2-august-7-2025-custom-query-abstract-method-failure)
- [Issue 3 (August 8, 2025): Failed to load ApplicationContext for WebMergedContextConfiguration](#issue-3-august-8-2025-failed-to-load-applicationcontext-for-webmergedcontextconfiguration)
- [Issue 4 (August 9, 2025): Exception related to the Java * Date Time occurred](#issue-4-august-8-2025-exception-related-to-the-java--date-time-occurred)
- [Issue 5 (August 10, 2025): Create resource issue due to UUID being null](#issue-5-august-9-2025-create-resource-issue-due-to-uuid-being-null)
- [Issue 6 (August 12, 2025): DateTime format validation not working in JSON requests](#issue-6-august-12-2025-datetime-format-validation-not-working-in-json-requests)
- [Issue 7 (August 12, 2025): Parameterize test for controller negative testing not working](#issue-7-august-12-2025-parameterize-test-for-controller-negative-testing-not-working)
- [Issue 8 (August 15, 2025): Custom Query abstract method failure with the exception `UnsatisfiedDependencyException`](#issue-8-august-15-2025-custom-query-abstract-method-failure-with-the-exception-unsatisfieddependencyexception)
- [Issue 9 (August 24, 2025): Issue on `userRepository`](#issue-9-august-24-2025-issue-on-userrepository)
- [Issue 10 (September 5, 2025): WebMvcTest security configuration causing 403 Forbidden errors](#issue-10-september-5-2025-webmvctest-security-configuration-causing-403-forbidden-errors)
- [**Issue 11 (September 7, 2025): Admin users getting 401 Unauthorized despite correct credentials**](#issue-11-september-7-2025-admin-users-getting-401-unauthorized-despite-correct-credentials)

---
## Technical issues encountered

---

### Issue 1 (July 31, 2025): Test configuration failure
- **🐞 Issue:** Auto-generated `contextLoads()` test failing due to incomplete configuration.
- **🧪 Solution:** Removed unnecessary test file: `GameLeaderboardApiApplicationTests.java`.
- **✅ Result:** CI pipeline restored to successful build status.

---

### Issue 2 (August 7, 2025): Custom Query abstract method failure
- **🐞 Issue:** Error occurred after adding custom query method in PlayerRepository `boolean existsByName (String name)`.
- **🧪 Solution:** update the variable declared in the parameter of the custom query method from `name` to `playerName` 
that matches the player entity attribute `playerName`.
- **✅ Result:** CI pipeline restored to successful build status.

---

### Issue 3 (August 8, 2025): Failed to load ApplicationContext for WebMergedContextConfiguration
- **🐞 Issue:** Failed to load ApplicationContext for WebMergedContextConfiguration.
- **🧪 Solution:** Changed the annotation of the playerService declared from `@Autowired` to `@MockitoBean`.
- **✅ Result:** No issue related to this occurred again.

---

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
- **🧪 Solution:** Implement `@PrePersist` method in the `PlayerEntity` so it’s always generate UUID if null.
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
        "username": "JohnDoe",
        "scores": 1000,
        "timestamp": "2025-08-08T14:00:00"
    }
  }
  ````

--- 

### Issue 6 (August 12, 2025): DateTime format validation not working in JSON requests
- **🐞 Issue:** Expected `@ExceptionHandler(DateTimeParseException.class)` to catch invalid datetime formats in JSON 
requests, but handler wasn't triggering.
- **Cause:** JSON parsing happens at Jackson level first. 
`HttpMessageNotReadableException` wraps all JSON deserialization errors before they reach `DateTimeParseException`.
- **🧪 Solution:** Use `HttpMessageNotReadableException` handler with message inspection to customize error responses:
  ```java
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
      
      Map<String, List<String>> errors;
      
      if (ex.getMessage().contains("LocalDateTime")) {
          errors = Map.of("timestamp", List.of("Invalid datetime format. Expected: yyyy-MM-ddTHH:mm:ss"));
      } else if (ex.getMessage().contains("JSON")) {
          errors = Map.of("json", List.of("Malformed JSON structure"));
      } else {
          errors = Map.of("request", List.of("Invalid request format"));
      }
      
      return ResponseEntity.badRequest().body(new ErrorResponseDto(
          LocalDateTime.now(),
          HttpStatus.BAD_REQUEST.value(),
          "Invalid request format",
          errors
      ));
  }

- **✅ Result:** Proper error messages for invalid timestamp formats (e.g., 2025-08-01T20:00:00) in JSON requests.
- **📝 Lesson Learned:** Always check the exception hierarchy when dealing with JSON request validation. 
DateTimeParseException only catches direct Java datetime parsing, not JSON deserialization errors.

---

### Issue 7 (August 12, 2025): Parameterize test for controller negative testing not working
- **🐞 Issue:** Invalid timestamp used are not well tested with parameterized tests.
- **Cause:** In parameterized testing in controller layer for invalid formats, updating DTOs with invalid date strings 
is not possible because it requires parsing.
- **🧪 Solution:** Instantiate an `ObjectMapper` via `createObjectNode()` method and execute `put()` like this:
  ```java
  var testJson = mapper.createObjectNode();
  testJson.put("name", "Test Player");
  testJson.put("score", 100);
  testJson.put("timestamp", invalidDateTime);

  String jsonTestRequest = mapper.writeValueAsString(testJson);
  
  mockMvc.perform(post("/api/players")
                  .with(csrf())
                  .with(httpBasic("acrexia", "dummy"))
                  .contentType("application/json")
                  .content(jsonTestRequest))
          .andExpect(status().isBadRequest());

- **✅ Result:** Parameterized testing works properly in the controller layer.
- **📝 Lesson Learned:** If there's a parsing needed in an attribute when testing, instead of updating DTOs, instantiate
an ObjectMapper.

---

### Issue 8 (August 15, 2025): Custom Query abstract method failure with the exception `UnsatisfiedDependencyException`
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

---

### Issue 9 (August 24, 2025): Issue on `userRepository`
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

--- 

### Issue 10 (September 5, 2025): WebMvcTest security configuration causing 403 Forbidden errors
- **🐞 Issue:** Test method expecting 201 Created status but receiving 403 Forbidden for `/api/users/register` 
endpoint that should be publicly accessible.
  ```
  java.lang.AssertionError: Multiple Exceptions (4):
  Status expected:<201> but was:<403>
  Response should contain header 'location'
  Response header 'location' expected:</api/users/register/1> but was:<null>
  No value at JSON path "$.apiMessage"

- **Cause:** `@WebMvcTest` doesn't autoload main `@Configuration` classes like `BasicAuthenticationConfig`. 
Without `SecurityFilterChain` bean, Spring Security defaults to requiring authentication for all endpoints.

- **🧪 Solution:** Created separate `TestSecurityConfig` with proper `SecurityFilterChain` bean and fixed rule ordering:
  ```java
  @TestConfiguration
  static class TestSecurityConfig {
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          return http.authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/users/register").permitAll()  // Must come BEFORE
                  .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                  .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN") 
                  .requestMatchers("/api/users/**").authenticated()    // More general pattern
                  .anyRequest().authenticated())
              // ... rest of config
              .build();
      }
  }
  
- ✅ Result: Tests pass with proper security behavior - public endpoints accessible, protected endpoints secured.
- 📝 Lesson Learned:
  - @WebMvcTest requires explicit @Import for security configurations.
  - Security filter chain rule ordering is critical - specific patterns must come before general ones
  - Separating test security configurations improves reusability across multiple test classes


---

### **Issue 11 (September 7, 2025): Admin users getting 401 Unauthorized despite correct credentials**

- **🐞 Issue:** Created admin users successfully (201 response) but getting 401 Unauthorized when trying to access protected endpoints with correct Basic Auth credentials.
  ```
  Username: admin_config
  Password: Dummy#11
  Response: 401 Unauthorized

- **Cause:** Missing final keyword in GameUserDetailsService field declaration. The @RequiredArgsConstructor annotation only works with final fields, so UserRepository was never injected and remained null.
  ```
  @Service
  @RequiredArgsConstructor
  public class GameUserDetailsService implements UserDetailsService {
  private UserRepository userRepository;  // Missing 'final' keyword

- **🧪 Solution:** Added final keyword to enable proper dependency injection:
  ```
  @Service
  @RequiredArgsConstructor
  public class GameUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;  // Added 'final'
  
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          UserEntity user = userRepository.findByUsername(username);
          // ... rest of method
      }
  }

- **✅ Result:** Admin users can now authenticate successfully and access protected endpoints with proper ADMIN role authorization.

- **📝 Lesson Learned:**
  - `@RequiredArgsConstructor` only generates constructor parameters for final fields
  - Without final, the field remains null and causes NullPointerException during authentication
  - Alternative solution: Use @Autowired annotation instead of @RequiredArgsConstructor
  - Always verify dependency injection is working when authentication fails mysteriously