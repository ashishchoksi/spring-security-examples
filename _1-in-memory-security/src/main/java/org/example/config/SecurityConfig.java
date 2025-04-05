package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    /**
     * password encoder require to encode password stored in memory
     * if you not create bean for this then use {bcrypt}<your-pwd> while creating user
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * this will create temp user for testing
     * if you use password encoder make sure encode password with same encoding bcrypt here
     * when request comes it checks pwd encoder and encode pwd from request and compare with in memory encoded pwd
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.withUsername("ashish")
                .password(new BCryptPasswordEncoder().encode("my_pwd")) // you have to map password encoder {noop} with plain text or create pwd encoder bean
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1);
    }

}
