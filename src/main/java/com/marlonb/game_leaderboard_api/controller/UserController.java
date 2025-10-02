package com.marlonb.game_leaderboard_api.controller;

import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.user.*;
import com.marlonb.game_leaderboard_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<ApiMessageResponseDto<UserResponseDto>> createNewUser (@Valid @RequestBody
                                                                                 UserRequestDto userRequest) {

        // Save new user data and build a Location URI for the created resource
        UserResponseDto createUserRequest = userService.createUser(userRequest);
        URI location = URI.create("/api/users/register/" + createUserRequest.id());

        return ResponseEntity.created(location).body(new ApiMessageResponseDto<>
                                                     ("User created successfully!",
                                                      createUserRequest));
    }

    @PostMapping("/users/login")
    public ResponseEntity<ApiMessageResponseDto<String>> loginUser (@Valid @RequestBody LoginRequestDto loginRequest) {

        String verifiedUser = userService.verifyUser(loginRequest);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("User login successfully!",
                                         verifiedUser));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiMessageResponseDto<UserResponseDto>> createAdminUser (@Valid @RequestBody
                                                                                   AdminUserRequestDto adminRequest) {

        // Save new admin data and build a Location URI for the created resource
        UserResponseDto createAdminRequest = userService.createAdminUser(adminRequest);
        URI location = URI.create("/api/users/" + createAdminRequest.id());

        return ResponseEntity.created(location).body(new ApiMessageResponseDto<>
                                                     ("Admin created successfully!",
                                                      createAdminRequest));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiMessageResponseDto<List<UserResponseDto>>> retrieveListOfUsers () {

        List<UserResponseDto> listOfUsers = userService.retrieveAllUsers();

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Retrieved all users successfully!",
                                         listOfUsers));
    }

    // Allows access to the user with ADMIN role or the user whose ID matches the authenticated user's ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiMessageResponseDto<UserResponseDto>> retrieveSpecificUserData (@PathVariable long id) {

        UserResponseDto retrievedUser = userService.retrieveSpecificUser(id);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Retrieved specific user successfully!",
                                         retrievedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiMessageResponseDto<UserResponseDto>> updateSpecificUserData (@PathVariable long id,
                                                                                          @Valid @RequestBody
                                                                                          UserUpdateDto userUpdate) {

        UserResponseDto updatedUser = userService.updateSpecificUser(id, userUpdate);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Updated specific user successfully!",
                                         updatedUser));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteSpecificUserData (@PathVariable long id) {

        userService.deleteSpecificUser(id);

        return ResponseEntity.noContent().build();
    }

    /* --- Self endpoints --- */
    @GetMapping("/users/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiMessageResponseDto<UserSummaryDto>> getUserProfile (Authentication auth) {

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Long userProfileId = principal.getId();

        if (userProfileId == null) {
            throw new ResourceNotFoundException("User not found.");
        }

        UserSummaryDto userProfile = userService.getUserProfile(userProfileId);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Retrieved user profile successfully!",
                                                userProfile));
    }

    @PutMapping("/users/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiMessageResponseDto<UserSummaryDto>> updateUserProfile (Authentication auth,
                                                                                    @Valid @RequestBody
                                                                                    UserUpdateDto updateRequest) {

        UserPrincipal principal  = (UserPrincipal) auth.getPrincipal();
        Long userProfileId = principal.getId();

        if (userProfileId == null) {
            throw new ResourceNotFoundException("User not found.");
        }

        UserSummaryDto userProfile = userService.updateUserProfile(userProfileId, updateRequest);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Update user profile successfully!",
                                         userProfile));
    }
}
