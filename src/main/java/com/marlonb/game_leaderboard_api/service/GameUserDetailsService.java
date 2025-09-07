package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserPrincipal;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.marlonb.game_leaderboard_api.service.ServiceErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GameUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND.getErrorMessage());
        }

        return new UserPrincipal(user);
    }
}
