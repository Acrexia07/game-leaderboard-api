package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserMapper;
import com.marlonb.game_leaderboard_api.model.user.UserRequestDto;
import com.marlonb.game_leaderboard_api.model.user.UserResponseDto;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto createUser (UserRequestDto userRequest) {

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
}
