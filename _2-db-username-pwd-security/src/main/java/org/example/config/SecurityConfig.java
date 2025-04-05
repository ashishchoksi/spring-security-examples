package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                .regexMatchers("/user/register").permitAll()
                .anyRequest().authenticated()
        )
                .csrf(csfr -> csfr.disable())
                .httpBasic(Customizer.withDefaults()).build();
    }

}
