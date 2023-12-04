package com.taldate.backend.auth.controller;

import com.taldate.backend.auth.dto.LoginDTO;
import com.taldate.backend.auth.dto.LoginResponseDTO;
import com.taldate.backend.auth.dto.RegisterDTO;
import com.taldate.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public LoginResponseDTO login(@RequestBody LoginDTO dto) {
        return service.login(dto);
    }

}