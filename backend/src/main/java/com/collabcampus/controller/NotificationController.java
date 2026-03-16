package com.collabcampus.controller;
import com.collabcampus.service.*;
import com.collabcampus.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/notifications") @RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notifService;
    private final UserService userService;

    @GetMapping public ResponseEntity<?> getAll(@AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(notifService.getForUser(u.getId()));
    }

    @GetMapping("/unread-count") public ResponseEntity<?> unreadCount(@AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(java.util.Map.of("count", notifService.getUnreadCount(u.getId())));
    }

    @PutMapping("/mark-all-read") public ResponseEntity<?> markRead(@AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        notifService.markAllRead(u.getId());
        return ResponseEntity.ok(java.util.Map.of("message","All marked read"));
    }
}
