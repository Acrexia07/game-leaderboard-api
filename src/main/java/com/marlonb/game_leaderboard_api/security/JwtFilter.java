package com.marlonb.game_leaderboard_api.security;

import com.marlonb.game_leaderboard_api.service.GameUserDetailsService;
import com.marlonb.game_leaderboard_api.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final GameUserDetailsService gameUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract the "Authorization" header from the request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the header contains a Bearer token, then extract the JWT
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // remove "Bearer " prefix
            username = jwtService.extractUsername(token); // extract subject (username) from JWT
        }

        // If we have a username and no authentication is set in the SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from the database (Spring Security UserDetails object)
            UserDetails userDetails = gameUserDetailsService.loadUserByUsername(username);

            // Validate the token against the user details
            if (jwtService.isTokenValid(token, userDetails.getUsername())) {

                // Create an authentication token for the user with their authorities
                var authToken = new UsernamePasswordAuthenticationToken
                                    (userDetails, null, userDetails.getAuthorities());

                // Attach request-specific details (like IP, session, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authenticated user in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
