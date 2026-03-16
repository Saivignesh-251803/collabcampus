package com.collabcampus.service;

import com.collabcampus.entity.*;
import com.collabcampus.enums.Role;
import com.collabcampus.repository.*;
import com.collabcampus.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public Map<String,Object> login(String email, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepo.findByEmail(email).orElseThrow();
        if (!user.isActive()) throw new RuntimeException("Account is disabled");
        String accessToken = jwtUtil.generateToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);
        refreshTokenRepo.deleteByUserId(user.getId());
        refreshTokenRepo.save(RefreshToken.builder()
            .token(refreshToken).user(user)
            .expiresAt(LocalDateTime.now().plusDays(7))
            .build());
        return Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken,
            "user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().name()
            )
        );
    }

    public Map<String,String> refresh(String refreshToken) {
        RefreshToken rt = refreshTokenRepo.findByToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Refresh token expired");
        String newAccess = jwtUtil.generateToken(rt.getUser().getEmail());
        return Map.of("accessToken", newAccess);
    }

    public void register(String name, String email, String password) {
        if (userRepo.existsByEmail(email)) throw new RuntimeException("Email already registered");
        userRepo.save(User.builder()
            .name(name).email(email)
            .password(passwordEncoder.encode(password))
            .role(Role.STUDENT).active(true)
            .build());
    }
}
