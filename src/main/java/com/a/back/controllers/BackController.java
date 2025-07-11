package com.a.back.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackController {
    @GetMapping("/api/home")
    public String home(@AuthenticationPrincipal Jwt jwt){
        String username = jwt.getClaim("preferred_username");
        return "Hello, "+ username;
    }


}
