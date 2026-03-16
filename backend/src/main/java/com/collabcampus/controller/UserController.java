package com.collabcampus.controller;
import com.collabcampus.entity.User;
import com.collabcampus.enums.Role;
import com.collabcampus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/users") @RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(userService.getByEmail(ud.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@AuthenticationPrincipal UserDetails ud, @RequestBody Map<String,String> req) {
        User u = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(userService.updateUser(u.getId(), req.get("name"), req.get("phone"), req.get("designation")));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() { return ResponseEntity.ok(userService.getAllUsers()); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody Map<String,Object> req) {
        User u = userService.createUser(
            (String)req.get("name"), (String)req.get("email"), (String)req.get("password"),
            Role.valueOf((String)req.get("role")),
            req.get("departmentId") != null ? Long.valueOf(req.get("departmentId").toString()) : null,
            (String)req.get("designation"));
        return ResponseEntity.ok(u);
    }

    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleUser(@PathVariable Long id) {
        userService.toggleActive(id);
        return ResponseEntity.ok(Map.of("message","User status updated"));
    }

    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody Map<String,String> req) {
        userService.resetPassword(id, req.get("password"));
        return ResponseEntity.ok(Map.of("message","Password reset successful"));
    }

    @GetMapping("/faculty")
    public ResponseEntity<?> getFaculty() { return ResponseEntity.ok(userService.getByRole(Role.FACULTY)); }
}
