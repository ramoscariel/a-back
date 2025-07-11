package com.a.back.controllers;

import com.a.back.entities.User;
import com.a.back.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@AuthenticationPrincipal Jwt jwt) {
        String sub = jwt.getSubject(); // token "sub"

        userRepo.findById(sub).orElseGet(() -> {
            User user = new User();
            user.setId(sub);
            return userRepo.save(user);
        });

        return ResponseEntity.ok().build();
    }
}