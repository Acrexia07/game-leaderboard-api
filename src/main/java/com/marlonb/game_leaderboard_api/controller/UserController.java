package com.marlonb.game_leaderboard_api.controller;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserMapper;
import com.marlonb.game_leaderboard_api.model.user.UserRequestDto;
import com.marlonb.game_leaderboard_api.model.user.UserResponseDto;
import com.marlonb.game_leaderboard_api.service.UserService;
import com.marlonb.game_leaderboard_api.utils.ApiMessageResponseDto;
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

    @GetMapping("/users")
    public ResponseEntity<ApiMessageResponseDto<List<UserResponseDto>>> retrieveListOfUsers () {

        List<UserResponseDto> listOfUsers = userService.retrieveAllUsers();

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Retrieved all users successfully!",
                                         listOfUsers));
    }
}
