package com.marlonb.game_leaderboard_api.security;

import com.marlonb.game_leaderboard_api.service.GameUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class BasicAuthenticationConfig {

    private final GameUserDetailsService gameUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws  Exception {

        return http.authorizeHttpRequests(
                auth -> auth
                                    .requestMatchers("/api/users/register").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                                    .requestMatchers("/api/users/**").authenticated()
                                    .requestMatchers("/api/players/**", "/api/leaderboards").authenticated()
                                    .anyRequest().authenticated())
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    @Bean
    public UserDetailsService userDetailsService () {
        return gameUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider (UserDetailsService userDetailsService,
                                                          PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
       return config.getAuthenticationManager();
    }
}
