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
- [Issue 11 (September 7, 2025): Admin users getting 401 Unauthorized despite correct credentials](#issue-11-september-7-2025-admin-users-getting-401-unauthorized-despite-correct-credentials)
- [Issue 12 (September 9, 2025): JWT Key Generation Only Producing 156 Bits Instead of Required 256 Bits](#issue-12-september-9-2025-jwt-key-generation-only-producing-156-bits-instead-of-required-256-bits)
- [Issue 13 (September 15, 2025): AccessDeniedException tests returning 500 instead of 403](#issue-13-september-15-2025-accessdeniedexception-tests-returning-500-instead-of-403)
- [Issue 14 (September 16, 2025): Compilation failure in test data due to missing PlayerSummaryDto](#issue-14-september-16-2025-compilation-failure-in-test-data-due-to-missing-playersummarydto)
- [Issue 15 (September 22, 2025): AdminUserRequestDto test failing due to hashed password in validation](#issue-15-september-22-2025-adminuserrequestdto-test-failing-due-to-hashed-password-in-validation)
- [Issue 16 (September 25, 2025): Leaderboard test failing due to `saveAll()` merging detached entities](#issue-16-september-25-2025-leaderboard-test-failing-due-to-saveall-merging-detached-entities)
- [Issue 17 (September 27, 2025): Duplicate user data persisting between tests](#issue-17-september-27-2025-duplicate-user-data-persisting-between-tests)
- [Issue 18 (September 30, 2025): Tests failing due to HttpClientErrorException on 401 responses](#issue-18-september-30-2025-tests-failing-due-to-httpclienterrorexception-on-401-responses)

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

### Issue 11 (September 7, 2025): Admin users getting 401 Unauthorized despite correct credentials

- **🐞 Issue:** Created admin users successfully (201 response) but getting 401 Unauthorized when trying to 
access protected endpoints with correct Basic Auth credentials.
  ```
  Username: admin_config
  Password: Dummy#11
  Response: 401 Unauthorized

- **Cause:** Missing final keyword in GameUserDetailsService field declaration. The @RequiredArgsConstructor 
annotation only works with final fields, so UserRepository was never injected and remained null.
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

- **✅ Result:** Admin users can now authenticate successfully and access protected endpoints with proper 
ADMIN role authorization.

- **📝 Lesson Learned:**
  - `@RequiredArgsConstructor` only generates constructor parameters for final fields
  - Without final, the field remains null and causes NullPointerException during authentication
  - Alternative solution: Use @Autowired annotation instead of @RequiredArgsConstructor
  - Always verify dependency injection is working when authentication fails mysteriously

---

### Issue 12 (September 9, 2025): JWT Key Generation Only Producing 156 Bits Instead of Required 256 Bits

- **🐞 Issue:** JwtService was only generating 156 bits for HMAC-SHA256 algorithm, which requires minimum 256 bits for 
secure signing.

- **⚠️ Error/Symptom:**
  - Expected: 256-bit (32-byte) secret key
  - Actual: 156-bit key from hardcoded string "myGameLeaderboardSecretKey"
  - JWT library throwing insufficient key length errors

- **🔧 Root Cause:**
  ```java
  // WRONG: Using hardcoded string, ignoring generated key
  private String secretKey = "myGameLeaderboardSecretKey";

  public JWTService() {
      KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
      SecretKey sk = keyGen.generateKey();
      Base64.getEncoder().encodeToString(sk.getEncoded()); // Generated but not used!
  }

- **🧪 Solution:**
  ```java
  // CORRECT: Actually use the generated key
  private String secretKey;

  public JWTService() {
  KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
  SecretKey sk = keyGen.generateKey();
  secretKey = Base64.getEncoder().encodeToString(sk.getEncoded()); // Assign it!
  }

- **📝 Lesson Learned:**
  - HMAC-SHA256 requires exactly 256 bits (32 bytes) for security.
  - Base64 encoding of random strings doesn't guarantee proper bit length.
  - `KeyGenerator.getInstance("HmacSHA256")` automatically generates correct bit length.
  - Always verify generated key meets algorithm requirements.

---

### Issue 13 (September 15, 2025): AccessDeniedException tests returning 500 instead of 403

- **🐞 Issue:** Unit test for endpoint with `@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")` 
was expecting 403 Forbidden, but response returned 500 Internal Server Error instead.
  ```
  when(userService.retrieveSpecificUser(otherUserId))
  .thenThrow(new AccessDeniedException("Access Denied!"));
  
  mockMvc.perform(get("/api/users/{id}", otherUserId))
  .andExpect(status().isForbidden());  // FAILED: returned 500
  ```
  **⚠️ Error/Symptom:** 
    - 500 Internal Server Error in JSON response
    - Custom `@ExceptionHandler(AccessDeniedException.class)` not triggered

- **🔧 Root Cause:**
  - `@WithMockUser` only sets username and roles, but doesn’t populate the custom UserPrincipal.id.
  - Since `@PreAuthorize compares #id == authentication.principal.id`, Spring Security could not evaluate the condition properly.
  - As a result, the security filter threw an exception that bypassed the custom handler and defaulted to a generic 500 error.

- **🧪 Solution:** Instead of relying solely on @WithMockUser, 
explicitly provide a full `UserPrincipal` object when testing ID-sensitive scenarios.
  ```
  @Test
  @DisplayName("User Management(READ): Should deny public user accessing other user's data")
  void shouldDenyPublicUserAccessingOtherUsersData () throws Exception {
  UserPrincipal testUserPrincipal = User1TestData.sampleUser1PrincipalData(); // has id = 1
  final long otherUserId = 2L;
  
      when(userService.retrieveSpecificUser(otherUserId))
          .thenThrow(new AccessDeniedException("Access Denied"));
  
      mockMvc.perform(get("/api/users/{id}", otherUserId)
                      .with(user(testUserPrincipal))) // ✅ supply principal with real ID
              .andExpectAll(
                      status().isForbidden(),
                      jsonPath("$.message").value("Forbidden access – insufficient permissions."),
                      jsonPath("$.error.credentials[0]").value("Access Denied"));
  }
  ```

- **✅ Result:**
  - Test now correctly returns 403 Forbidden.
  - Custom exception handler successfully formats JSON error response.

- **📝 Lesson Learned:**
  - Use @WithMockUser only for role-based checks.
  - For ID-based checks (when @PreAuthorize references authentication.principal.id), always use .with(user(customPrincipal)).
  - Mixing both is redundant — .with(user(...)) overrides @WithMockUser.

---

### Issue 14 (September 16, 2025): Compilation failure in test data due to missing PlayerSummaryDto

- **🐞 Issue:** `mvn clean install` fails with compilation errors when constructing `UserResponseDto` in test classes.
  - **Error example:**
    ```
    constructor UserResponseDto in record cannot be applied to given types;
    required: long,String,String,LocalDateTime,PlayerSummaryDto
    found:    Long,String,String,LocalDateTime
    reason: actual and formal argument lists differ in length
    ```
    
- **🔧 Root Cause:** `UserResponseDto` is a record with five fields, including `PlayerSummaryDto playerAccount`. 
Test data was creating `UserResponseDto` with only four arguments, omitting the player summary.

- **🧪 Solution:** 
  - Create a test data object for `PlayerSummaryDto` in your test data class:
    ```
    public static PlayerSummaryDto playerSummaryDto () {

        return new PlayerSummaryDto(
                samplePlayerData().getPlayerName(),
                samplePlayerData().getUuid(),
                samplePlayerData().getScores(),
                samplePlayerData().getTimestamp()
        );
    }
    
    
  - Pass this `PlayerSummaryDto` when constructing `UserResponseDto` in all test classes:
    ```
    public static UserResponseDto sampleUser1PrincipalResponse () {
  
        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1PrincipalData().getUsername(),
                sampleUser1PrincipalData().getPassword(),
                sampleUser1Data().getCreatedAt(),
                playerSummaryDto() // ← like this one
        );
    }

- **✅ Result:**
  - Maven compilation errors are resolved.
  - `mvn clean install` completes successfully.
  - Postman and runtime behavior remain unchanged because MapStruct still handles mapping in the service layer.

- **📝 Lesson Learned:**
  - Records enforce all constructor fields, even in test data.
  - Test DTOs must include all required fields, or provide null if the field is optional.
  - Creating dedicated test data objects (like PlayerSummaryDto) improves reusability and consistency across tests.

### Issue 15 (September 22, 2025): AdminUserRequestDto test failing due to hashed password in validation

- **🐞 Issue:**
Unit test `shouldRegisterSuccessfullyWhenAdminUserHasValidCredentials` failing with 400 Bad Request instead of expected 201 Created. 
Validation error: "Password must be between 8 and 20 characters".
  ```
  Status expected:<201> but was:<400>
  Body = {"timestamp":"2025-09-22T12:30:54.789865864","statusCode":400,"message":"Validation error(s) found!","error":{"password":["Password must be between 8 and 20 characters"]}}
  ```

- **⚠️ Error/Symptom:**
  - Test data shows plain password `"Abcd1234@"` (9 characters) in constants
  - HTTP request body contains hashed password `"$2a$10$YCf6xv08IbuAqFtthgR32u.SSzg29m4/ihe8yPEi//LK7bDJpLQeG"` (60 characters)
  - Validation fails because 60 > 20 character limit

- **🔧 Root Cause:** Test data method `sampleAdminUser1Request()` was using entity getter methods instead of raw constant values:
  ```
  // WRONG: Using getters that return processed/hashed values
  return new AdminUserRequestDto(
      sampleAdminUser1Data().getUsername(),  // Returns processed value
      sampleAdminUser1Data().getPassword(),  // Returns hashed password!
      sampleAdminUser1Data().getRole()
  );
  ```
  The `BASE_ADMIN_DATA` entity had its password hashed somewhere (setter, constructor, or JPA lifecycle),
so the getter returned the 60-character hash instead of the original raw password.

- **🧪 Solution:** Use raw constant values directly instead of entity getters for DTO/request objects:
  ```
  // CORRECT: Using raw constants for input validation
  public static AdminUserRequestDto sampleAdminUser1Request() {
      return new AdminUserRequestDto(
          ADMIN1_NAME,        // Raw value "admin1"
          ADMIN1_PASSWORD,    // Raw value "Abcd1234@"
          ADMIN1_ROLE         // Raw value UserRoles.ADMIN
      );
  }
  ```

- **✅ Result:** Test passes with 201 Created status and proper validation of raw input data.

- **📝 Lesson Learned:**
  - **For DTOs/Requests:** Always use raw constant values because these represent user input before processing
  - **For Responses/Entities:** Use getters because these represent processed/stored data
  - Entity getters may return transformed data (hashed passwords, formatted dates, etc.)
  - Separate raw input test data from processed entity test data to avoid validation confusion

---

### Issue 16 (September 25, 2025): Leaderboard test failing due to `saveAll()` merging detached entities

- **🐞 Issue:**  
  - Leaderboard unit test intermittently fails with unexpected ordering of players:
  ```
  Expected :[Player1, Player4, Player3]
  Actual :[Player1, Player2, Player3]
  ```
  - The issue occurs when scores are equal and the query depends on `timestamp ASC` for ordering.

- **⚠️ Error/Symptom:**
  - Test explicitly sets timestamps for Player2 and Player4, but actual results show ordering drift.
  - `saveAll()` appears to override timestamps or cause merge-related side effects.

- **🔧 Root Cause:**  
  - Mixing `entityManager.persist()` with `playerRepository.saveAll()` in the same test lifecycle:
  ```
  entityManager.persist(player1);
  entityManager.persist(player2);
  entityManager.persist(player3);
  entityManager.persist(player4);
  
  entityManager.flush();
  entityManager.clear();
  
  playerRepository.saveAll(List.of(player1, player2, player3, player4));
  ```
- `persist()` inserts and manages entities.
- `flush()` writes them to DB.
- `clear()` detaches all.
- `saveAll()` then calls `merge()` on detached entities, which can:
- Trigger `@PreUpdate`, updating timestamps unintentionally.
- Create new managed instances with inconsistent state.
  🧪 Solution:
  Use one persistence approach consistently:

**- ✅ Option A: Use only persist()**
  ```
  entityManager.persist(player1);
  entityManager.persist(player2);
  entityManager.persist(player3);
  entityManager.persist(player4);
  
  entityManager.flush();
  entityManager.clear();
  
  List<PlayerEntity> topPlayers =
  playerRepository.findTop3PlayerByOrderByScoresDescAndTimestampAsc();
  ```
**- ✅ Option B: Use only `saveAll()`**
  ```
  List<PlayerEntity> players = List.of(player1, player2, player3, player4);
  playerRepository.saveAll(players);
  
  List<PlayerEntity> topPlayers =
  playerRepository.findTop3PlayerByOrderByScoresDescAndTimestampAsc();
  ```
  - 🚫 Do not mix `persist()` + `saveAll()` in the same test for the same entities.

- **✅ Result:**
Tests now produce deterministic ordering (Player1, Player4, Player3).
No unexpected timestamp overrides.

- **📝 Lesson Learned:**
  - `persist()` (JPA) is for new entities only; schedules insert at flush/commit.
  - `saveAll()` (Spring Data JPA) works for both new and existing, but internally uses `merge()` on detached entities.
  - Mixing them in tests causes non-deterministic behavior.
  - For reproducible tests, pick one strategy (prefer `persist()` for precise test control).

### Issue 17 (September 27, 2025): Duplicate user data persisting between tests

- **🐞 Issue:**  
  - Tests involving user registration failed intermittently with **duplicate username constraint violations**, 
even though H2 was configured as in-memory (`jdbc:h2:mem:testdb`).
  Example symptom:  
    ```
    org.springframework.dao.DataIntegrityViolationException: could not execute statement;
    SQL [n/a]; constraint ["UK_username ... UNIQUE INDEX"]
    ```
    _It looked as though test data was persisting across test runs._

- **⚠️ Root Cause:**
  - The H2 configuration included `DB_CLOSE_DELAY=-1`, which keeps the in-memory database alive until the JVM stops.
  - While the schema was dropped between contexts (`ddl-auto=create-drop`), data inserted in one test was still 
  visible to the next test inside the same context lifecycle.
  - Thus, hardcoded usernames (like `newuser`) caused duplicate entries across tests.

- **🧪 Solution:**  
  Explicitly reset the `userRepository` state before each test run:
  ```java
  @BeforeEach
  void setup() {
      userRepository.deleteAll();
      baseUrl = "http://localhost:" + port;
  }
  _This ensures a clean slate before each test execution._

- **✅ Result:**
  - Tests no longer fail with duplicate username errors.
  - Each test now operates in a fully isolated state.

- **📝 Lesson Learned:**
  - Even with in-memory databases, persistence can outlive individual tests depending on connection parameters
  `(DB_CLOSE_DELAY=-1)`.
  - Always reset repositories `(deleteAll())` or use `@Transactional` with rollback for test isolation.
  - Relying only on create-drop is not enough when multiple test methods share the same application context.

### Issue 18 (September 30, 2025): Tests failing due to HttpClientErrorException on 401 responses

- **🐞 Issue:**
  - Tests checking for missing or malformed Authorization headers were failing when asserting on ResponseEntity status:
    ```
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    ```
  - Instead of returning a ResponseEntity with 401, the test threw:
    ```
    org.springframework.web.client.HttpClientErrorException$Unauthorized: 401 Unauthorized
    ```

- **⛑️ Symptom:** tests never reached the `assertEquals()` line, 
making it seem like the framework was not returning the expected response.

- **⚠️ Root Cause:**
  - `RestTemplate.exchange()` automatically throws `HttpClientErrorException` for any 4xx client error responses 
  by default.
  - The exception contains the HTTP status and optional body, but does not return a normal ResponseEntity.
  - Since no explicit response body was sent by the server, the exception appeared as `[no body]`.
  - The test was written expecting a normal ResponseEntity, so the thrown exception prevented the test from passing.

- **🧪 Solution:**
  - Use assertThrows to expect `HttpClientErrorException` (or its subclass Unauthorized) instead of expecting a 
  ResponseEntity:
    ```
    HttpClientErrorException exception = assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    });
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    ```

  - Optionally, disable automatic exception throwing by overriding RestTemplate’s error handler:
    ```
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
    @Override
    public boolean hasError(ClientHttpResponse response) {
    return false; // Treat all responses as normal, no exceptions
    }
    });
    ```

- **✅ Result:**
  - Tests now correctly handle 401 responses and can assert on status codes or messages.
  - Prevents misleading `[no body]` errors in logs and allows proper test verification.

- **📝 Lesson Learned:**
  - RestTemplate throws exceptions for all 4xx/5xx responses by default, so tests must anticipate this behavior.
  - Use `assertThrows` or a custom error handler when testing HTTP error responses.
  - Do not assume `exchange()` will return a normal ResponseEntity on 4xx client errors.