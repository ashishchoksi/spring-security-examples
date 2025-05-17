package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.model.JwtAuthToken;
import org.example.model.LoginRequest;
import org.example.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * to generate new token using refresh-token
 *
 * in tnis filter you don't need to check request header Authorization
 */

@RequiredArgsConstructor
public class JwtRefreshFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // only care about /generate-token API
        if ( !request.getServletPath().equalsIgnoreCase("/user/refresh-token") ) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract the token
        String refreshToken = getRefreshToken(request);
        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // validate refresh-token
        // this will hit our jwt-provider
        JwtAuthToken jwtAuthToken = new JwtAuthToken(refreshToken);
        Authentication authenticate = authenticationManager.authenticate(jwtAuthToken);

        // generate normal token once refresh-token is valis and set as header
        if (authenticate.isAuthenticated()) {
            String authToken = jwtUtil.generateToken(authenticate.getName(), 15); // normal token
            response.setHeader("Authorization", "Bearer " + authToken);
        }
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
