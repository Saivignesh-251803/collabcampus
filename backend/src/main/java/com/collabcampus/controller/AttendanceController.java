package com.collabcampus.controller;
import com.collabcampus.service.*;
import com.collabcampus.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/attendance") @RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final UserService userService;

    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody Map<String,Object> req, @AuthenticationPrincipal UserDetails ud) {
        User faculty = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(attendanceService.createSession(
            Long.valueOf(req.get("courseId").toString()), faculty.getId()));
    }

    @PostMapping("/mark")
    public ResponseEntity<?> mark(@RequestBody Map<String,String> req, @AuthenticationPrincipal UserDetails ud) {
        User student = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(attendanceService.markAttendance(req.get("qrToken"), student.getId()));
    }

    @GetMapping("/report/{courseId}")
    public ResponseEntity<?> report(@PathVariable Long courseId, @AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(attendanceService.getReport(courseId, u.getId()));
    }

    @GetMapping("/session/{sessionId}/records")
    public ResponseEntity<?> sessionRecords(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getSessionRecords(sessionId));
    }
}
