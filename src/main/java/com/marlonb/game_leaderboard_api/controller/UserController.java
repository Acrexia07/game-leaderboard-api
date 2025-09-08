package com.marlonb.game_leaderboard_api.controller;

import com.marlonb.game_leaderboard_api.model.user.*;
import com.marlonb.game_leaderboard_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiMessageResponseDto<UserResponseDto>> retrieveSpecificUserData (@PathVariable long id) {

        UserResponseDto retrievedUser = userService.retrieveSpecificUser(id);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Retrieved specific user successfully!",
                                         retrievedUser));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiMessageResponseDto<UserResponseDto>>
                    updateSpecificUserData (@PathVariable long id, @Valid @RequestBody UserUpdateDto userUpdate) {

        UserResponseDto updatedUser = userService.updateSpecificUser(id, userUpdate);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Updated specific user successfully!",
                                         updatedUser));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteSpecificUserData (@PathVariable long id) {

        userService.deleteSpecificUser(id);

        return ResponseEntity.noContent().build();
    }
}
