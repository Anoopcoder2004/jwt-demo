package com.example.JWT_Auth_demo.controller;

import com.example.JWT_Auth_demo.dto.AuthRequest;
import com.example.JWT_Auth_demo.dto.RegisterRequest;
import com.example.JWT_Auth_demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
