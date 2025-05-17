package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.model.LoginRequest;
import org.example.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * step towards JWT token
 *
 * 1) create own filter to validate Id / pwd and return token back
 *
 * Intercept the request in java model -> LoginRequest
 * create Authenticator object UserNamePasswordAuthenticator(...) already there in frameword
 * this will return Authentication object - validate userName/pwd internally
 * once its good generate token and return back to user as response header
 */

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // only care about /generate-token API
        if ( ! request.getServletPath().equalsIgnoreCase("/user/generate-token") ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. intercept the userName password
        LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

        // 2. create auth token object to validate uname/pwd
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // 3. if successful then generate token and return
        if (authenticate.isAuthenticated()) {
            String token = jwtUtil.generateToken(authenticate.getName(), 15); // 15 min valid token
            response.setHeader("Authorization", "Bearer " + token);
        }
    }
}
