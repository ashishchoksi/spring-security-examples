package org.example.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/*
this object holds the token and pass to provider
 */
public class JwtAuthToken extends AbstractAuthenticationToken {

    private String token;
    public JwtAuthToken(String token) {
        super(null);
        setAuthenticated(false); // default not authenticated
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}
