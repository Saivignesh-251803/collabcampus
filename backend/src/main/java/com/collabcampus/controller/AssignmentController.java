package com.collabcampus.controller;
import com.collabcampus.service.*;
import com.collabcampus.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController @RequestMapping("/api/assignments") @RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignService;
    private final UserService userService;

    @GetMapping("/course/{courseId}") public ResponseEntity<?> byCourse(@PathVariable Long courseId) { return ResponseEntity.ok(assignService.getByCourse(courseId)); }
    @GetMapping("/my") public ResponseEntity<?> myAssignments(@AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(u.getRole().name().equals("FACULTY") ? assignService.getByFaculty(u.getId()) : assignService.getByStudent(u.getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String,Object> req, @AuthenticationPrincipal UserDetails ud) {
        User faculty = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(assignService.create(
            (String)req.get("title"), (String)req.get("description"),
            (Integer)req.get("maxMarks"),
            req.get("deadline") != null ? LocalDateTime.parse((String)req.get("deadline")) : null,
            Long.valueOf(req.get("courseId").toString()), faculty.getId()));
    }

    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<?> submit(@PathVariable Long assignmentId, @RequestBody Map<String,String> req, @AuthenticationPrincipal UserDetails ud) {
        User student = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(assignService.submit(assignmentId, student.getId(), req.get("content")));
    }

    @PutMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<?> grade(@PathVariable Long submissionId, @RequestBody Map<String,Object> req) {
        return ResponseEntity.ok(assignService.grade(submissionId, (Integer)req.get("marks"), (String)req.get("feedback")));
    }

    @PostMapping("/submissions/{submissionId}/appeal")
    public ResponseEntity<?> appeal(@PathVariable Long submissionId, @RequestBody Map<String,String> req, @AuthenticationPrincipal UserDetails ud) {
        User student = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(assignService.raiseAppeal(submissionId, student.getId(), req.get("reason")));
    }

    @PutMapping("/appeals/{appealId}/resolve")
    public ResponseEntity<?> resolve(@PathVariable Long appealId, @RequestBody Map<String,Object> req) {
        Integer newMarks = req.get("newMarks") != null ? (Integer)req.get("newMarks") : null;
        return ResponseEntity.ok(assignService.resolveAppeal(appealId, (String)req.get("response"), newMarks));
    }

    @GetMapping("/appeals/pending")
    public ResponseEntity<?> pendingAppeals() { return ResponseEntity.ok(assignService.getPendingAppeals()); }
}
