package com.a.back.controllers;

import com.a.back.entities.Message;
import com.a.back.entities.User;
import com.a.back.repositories.MessageRepository;
import com.a.back.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepo;
    private final UserRepository userRepo;


    @GetMapping
    public List<Message> getUserMessages(@AuthenticationPrincipal Jwt jwt) {
        return messageRepo.findByUserId(jwt.getSubject());
    }

    @PostMapping
    public Message createMessage(@AuthenticationPrincipal Jwt jwt, @RequestBody Message message) {
        User user = userRepo.findById(jwt.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        message.setUser(user);
        return messageRepo.save(message);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt, @RequestBody Message input) {
        Message message = messageRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!message.getUser().getId().equals(jwt.getSubject()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        message.setMessage(input.getMessage());
        return messageRepo.save(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Message message = messageRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!message.getUser().getId().equals(jwt.getSubject()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        messageRepo.delete(message);
        return ResponseEntity.noContent().build();
    }
}
