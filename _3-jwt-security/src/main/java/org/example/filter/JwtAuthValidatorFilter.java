package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.model.JwtAuthToken;
import org.example.model.LoginRequest;
import org.example.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * step towards JWT token validate
 *
 * we have to use framework with all required classes
 *
 * JwtAuthValidatorFilter -> to overall validate auth token
 *  - intercept token
 *  - create custom Token object ?
 *  - authenticate that object ?
 *
 * JwtAuthToken impl Abstract token
 * JwtAuthProvider to manage authenticate() method
 */

@RequiredArgsConstructor
public class JwtAuthValidatorFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = interceptToken(request);

        if (token != null) {

            // create auth token object to pass ahed
            JwtAuthToken jwtAuthToken = new JwtAuthToken(token);

            // pass token to manager to validate
            // since its custom token we have to create provider to handle .authenticate() method
            Authentication authenticate = authenticationManager.authenticate(jwtAuthToken);

            if (!authenticate.isAuthenticated()) {
                throw new BadCredentialsException("Invalid token found!");
            }

            // set authenticator to security context holder to use forward
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }

        filterChain.doFilter(request, response);
    }

    private String interceptToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            String header = request.getHeader("Authorization");
            if (header.startsWith("Bearer ")) {
                return header.substring(7);
            }
        }
        return null;
    }
}
