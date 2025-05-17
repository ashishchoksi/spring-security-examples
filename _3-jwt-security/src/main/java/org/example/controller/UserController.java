package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.UserEntity;
import org.example.service.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserAuthService userAuthService;

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserEntity userEntity) { // do not use entity directly here, use DTO and convert to entity
        userAuthService.save(userEntity);
        return ResponseEntity.ok().build();
    }

}
