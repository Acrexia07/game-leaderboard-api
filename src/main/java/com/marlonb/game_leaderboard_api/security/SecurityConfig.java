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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity // enables @PreAuthorize / @Secured annotations on methods
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final GameUserDetailsService gameUserDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws  Exception {

        return http.authorizeHttpRequests(
                auth -> auth
                                    .requestMatchers("/api/users/register").permitAll()
                                    .requestMatchers("/api/users/login").permitAll()
                                    .requestMatchers("/api/users/me").authenticated()
                                    .requestMatchers("/api/players/me").authenticated()
                                    .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.GET, "api/players").hasRole("ADMIN")
                                    .requestMatchers("/api/users/{id}").hasRole("ADMIN")
                                    .requestMatchers("/api/players/{id}").hasRole("ADMIN")
                                    .requestMatchers("/api/leaderboards").authenticated()
                                    .anyRequest().authenticated())
            // Disable CSRF (we rely on JWT for stateless auth)
            .csrf(AbstractHttpConfigurer::disable)
            // Allow basic auth (mainly for testing/debugging)
            .httpBasic(Customizer.withDefaults())
            // Add JWT filter before the standard username/password filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            // Make the app stateless (no HTTP sessions stored)
            .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    // Custom UserDetailsService (loads user data from DB)
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
