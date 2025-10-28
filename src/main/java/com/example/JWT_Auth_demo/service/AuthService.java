package com.example.JWT_Auth_demo.service;

import com.example.JWT_Auth_demo.dto.AuthRequest;
import com.example.JWT_Auth_demo.dto.RegisterRequest;
import com.example.JWT_Auth_demo.model.User;
import com.example.JWT_Auth_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);

        // ✅ Create UserDetails from entity
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // use email as username
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();

        return jwtService.generateToken(userDetails);
    }

    public String login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // ✅ Create UserDetails from entity
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();

        return jwtService.generateToken(userDetails);
    }
}
