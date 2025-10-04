package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.user.*;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.marlonb.game_leaderboard_api.service.ServiceErrorMessages.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponseDto createUser (@Valid @RequestBody UserRequestDto userRequest) {

        UserEntity createdUser = userMapper.toEntity(userRequest);

        //Checks if admin username already exists in the database
        if(userRepository.existsByUsername(createdUser.getUsername())) {
            throw new DuplicateResourceFoundException
                    (String.format(createdUser.getUsername(), DUPLICATE_USERNAME_FOUND));
        }

        // Encrypt password using BCrypt Password encoder
        createdUser.setPassword(encoder.encode(createdUser.getPassword()));

        createdUser.setRole(UserRoles.USER);
        UserEntity savedUser = userRepository.save(createdUser);
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponseDto createAdminUser (@Valid @RequestBody AdminUserRequestDto adminRequest) {

        UserEntity createdAdminUser = userMapper.toEntity(adminRequest);

        //Checks if admin username already exists in the database
        if(userRepository.existsByUsername(createdAdminUser.getUsername())) {
            throw new DuplicateResourceFoundException
                    (String.format(createdAdminUser.getUsername(), DUPLICATE_USERNAME_FOUND));
        }

        createdAdminUser.setPassword(encoder.encode(createdAdminUser.getPassword()));
        UserEntity savedUser = userRepository.save(createdAdminUser);
        return userMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> retrieveAllUsers () {

        List<UserEntity> listOfUsers = userRepository.findAll();

        return listOfUsers.stream()
                          .map(userMapper::toResponse)
                          .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto retrieveSpecificUser(long id) {

        UserEntity foundUser = findUserId(id);
        return userMapper.toResponse(foundUser);
    }

    @Transactional
    public UserResponseDto updateSpecificUser(long id,
                                              @Valid @RequestBody UserUpdateDto userUpdate) {

        UserEntity foundUser = findUserId(id);

        // Check if the updated username already exists in the database
        // and is different from the current user's username
        if(userRepository.existsByUsername(userUpdate.getUsername()) &&
           !foundUser.getUsername().equalsIgnoreCase(userUpdate.getUsername())) {
            throw new DuplicateResourceFoundException
                    (String.format(userUpdate.getUsername(), DUPLICATE_USERNAME_FOUND));
        }

        // Encrypt password using BCrypt Password encoder
        String encodePassword = encoder.encode(userUpdate.getPassword());
        userUpdate.setPassword(encodePassword);

        userMapper.toUpdateFromEntity(foundUser, userUpdate);
        UserEntity savedUser = userRepository.save(foundUser);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public void deleteSpecificUser(long id) {

        findUserId(id);
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UserSummaryDto getUserProfile (long id) {

        UserEntity foundUser = findUserId(id);
        return userMapper.toSummary(foundUser);
    }

    @Transactional
    public UserSummaryDto updateUserProfile (long id,UserUpdateDto updateRequest) {

        UserEntity foundUser = findUserId(id);

        if(userRepository.existsByUsername(updateRequest.getUsername()) &&
                !foundUser.getUsername().equalsIgnoreCase(updateRequest.getUsername())) {
            throw new DuplicateResourceFoundException
                    (String.format(updateRequest.getUsername(), DUPLICATE_USERNAME_FOUND));
        }

        String encodePassword = encoder.encode(updateRequest.getPassword());
        updateRequest.setPassword(encodePassword);

        userMapper.toUpdateFromEntity(foundUser, updateRequest);
        UserEntity savedUser = userRepository.save(foundUser);

        return userMapper.toSummary(savedUser);
    }

    public UserEntity findUserId (long id) {

        return userRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException
                                          (String.format(RESOURCE_NOT_FOUND.getErrorMessage(), id)));
    }

    public String verifyUser(@Valid @RequestBody LoginRequestDto loginRequest) {

        // Authenticate user credentials using AuthenticationManager.
        // If authentication succeeds, generate a JWT token for the username.
        // If authentication fails, throw a BadCredentialsException.
        try {
            Authentication authentication =
                    authManager.authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    ));

            return jwtService.generateToken(loginRequest.username());

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(INVALID_CREDENTIALS_FOUND.getErrorMessage());
        }

    }
}
