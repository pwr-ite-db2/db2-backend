package com.example.databasedemo2.security;

import com.example.databasedemo2.entitymanagement.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException, JwtException, EntityNotFoundException {
        final String jwtHeader = request.getHeader("Authorization");
        final String token;
        final String username;

        // check if request has a jwt
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);    // pass execution to the next filter
            return;
        }

        token = jwtHeader.substring(7); // start of the JWT

        try {
            username = jwtService.getUsername(token);   // get username (email) from the token, throws JWT exception if token is invalid
        } catch (JwtException e) {
            response.getWriter().println(new ObjectMapper().writeValueAsString(Map.of("cause", e.getMessage())));
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        // check if user is already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // get user from database, throws exception if user doesn't exist
            try {
                UserDetails user = userService.loadUserByUsername(username);

                // check if token is valid
                if (jwtService.isValidToken(token, user)) {
                    // update SecurityContextHolder
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (EntityNotFoundException | UsernameNotFoundException e) {
                response.getWriter().println(new ObjectMapper().writeValueAsString(Map.of("cause", e.getMessage())));
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }
        }

        // pass execution to the next filter
        filterChain.doFilter(request, response);
    }
}
