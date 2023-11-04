package com.taldate.backend.auth.controller;

import com.taldate.backend.auth.dto.LoginDTO;
import com.taldate.backend.auth.dto.RegisterDTO;
import com.taldate.backend.auth.exception.DuplicateUserException;
import com.taldate.backend.auth.exception.UnsuccessfulLoginException;
import com.taldate.backend.auth.service.AuthService;
import com.taldate.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public void register(@RequestBody RegisterDTO dto) {
        service.register(dto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO dto) {
        // temporary; return String
        return service.login(dto);
    }

    @ExceptionHandler({DuplicateUserException.class, UnsuccessfulLoginException.class})
    public ResponseEntity<Object> handleException(RuntimeException e) {
        // Expected errors returned as JSON {"error":"msg..."}, and messages shown in the front-end
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
}