package com.collabcampus.controller;
import com.collabcampus.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> req) {
        return ResponseEntity.ok(authService.login(req.get("email"), req.get("password")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> req) {
        authService.register(req.get("name"), req.get("email"), req.get("password"));
        return ResponseEntity.ok(Map.of("message","Registration successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String,String> req) {
        return ResponseEntity.ok(authService.refresh(req.get("refreshToken")));
    }
}
