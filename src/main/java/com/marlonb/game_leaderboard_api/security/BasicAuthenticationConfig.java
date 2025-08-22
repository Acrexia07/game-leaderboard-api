package com.marlonb.game_leaderboard_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class BasicAuthenticationConfig {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws  Exception {

        return http.authorizeHttpRequests(
                        auth -> auth.requestMatchers("/api/**")
                                .hasRole("ADMIN")
                                .anyRequest()
                                .permitAll())
                    .csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(Customizer.withDefaults())
                    .sessionManagement(session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .build();
    }

    @Bean
    public UserDetailsService userDetailsService () {
        return new InMemoryUserDetailsManager(
                User.builder()
                    .username("acrexia")
                    .password(passwordEncoder().encode("dummy"))
                    .authorities("ROLE_ADMIN")
                    .build()
        );
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider () {
//
//    }


    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}
