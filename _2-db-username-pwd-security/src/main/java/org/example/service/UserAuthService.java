package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * step 3)
 * when you add spring security dependency it automatically look for UserDetailsService Impl if Basic auth is used
 * this is main connection point of our own classes to spring class
 * spring will invoke function loadUserByUsername(userName) with userName that API caller will pass
 * rest is ours now we will take it and fetch from DB and give our version to spring that internally check with hashed password
 * since we provided password encoder
 */
@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public void save(UserEntity user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with name: " + username));
    }
}
