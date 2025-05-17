package org.example.provider;

import lombok.RequiredArgsConstructor;
import org.example.model.JwtAuthToken;
import org.example.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * AuthProvider has list of all providers (from @Bean we add our impl)
 * when you call authManager.authenticate(...) it will call support() for all providers
 * whichever says true that will invoke respective classes authenticate() method
 */
@RequiredArgsConstructor
public class JwtAuthValidatorProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * there should be one AuthToken object -> AuthTokenProvider mapping
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthToken jwtAuthToken = (JwtAuthToken) authentication; // convert to concrete class
        String token = jwtAuthToken.getToken();
        String userName = jwtUtil.extratUserName(token);

        if (userName == null) {
            throw new BadCredentialsException("Invalid token found!");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);// we are still making DB connection
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthToken.class.isAssignableFrom(authentication));
    }

}
