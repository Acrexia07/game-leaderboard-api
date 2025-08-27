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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
