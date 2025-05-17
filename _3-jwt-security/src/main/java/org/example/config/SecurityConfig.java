package org.example.config;

import org.example.filter.JwtAuthFilter;
import org.example.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * step 6)
 * whenever deal with spring security always create these 2 bean
 * password encoder will encode password to store and check encode password from input
 *
 * Security filter chain to skip/permit few endpoint
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // its spring provided Auth provider we are just creating object since we need it for Auth manager
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    /* we have to tell AuthManager to use DaoProvider only not others
    why Dao Auth only
    because we have created UserNamePasswordToken() that is handled by Dao Auth provider
    */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        return new ProviderManager(
                Arrays.asList(daoAuthenticationProvider(userDetailsService))
        );
    }

    /**
     * since framework dont know our filter we have created
     * we have to tell it
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil,
                                                   AuthenticationManager authenticationManager) throws Exception {

        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(authenticationManager, jwtUtil);
        return http.authorizeHttpRequests(auth -> auth
                .regexMatchers("/user/register").permitAll()
                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csfr -> csfr.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); // our filter will take care everything
    }

}
