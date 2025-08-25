package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.user.*;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto createUser (@Valid @RequestBody UserRequestDto userRequest) {

        UserEntity createdUser = userMapper.toEntity(userRequest);
        UserEntity savedUser = userRepository.save(createdUser);
        return userMapper.toResponse(savedUser);
    }

    public List<UserResponseDto> retrieveAllUsers () {

        List<UserEntity> listOfUsers = userRepository.findAll();
        return listOfUsers.stream()
                          .map(userMapper::toResponse)
                          .toList();
    }

    public UserResponseDto retrieveSpecificUser(long id) {

        UserEntity foundUser = checkUserId(id);
        return userMapper.toResponse(foundUser);
    }

    public UserResponseDto updateSpecificUser(long id,
                                              @Valid @RequestBody UserUpdateDto userUpdate) {

        UserEntity foundUser = checkUserId(id);

        userMapper.toUpdateFromEntity(foundUser, userUpdate);
        UserEntity savedUser = userRepository.save(foundUser);

        return userMapper.toResponse(savedUser);
    }

    public UserEntity checkUserId (long id) {

        return userRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException
                                          (String.format("This user id '%d' does not exist!", id)));
    }
}
