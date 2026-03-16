package com.collabcampus.controller;
import com.collabcampus.service.CourseService;
import com.collabcampus.entity.User;
import com.collabcampus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/courses") @RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    @GetMapping public ResponseEntity<?> getAll() { return ResponseEntity.ok(courseService.getAll()); }
    @GetMapping("/{id}") public ResponseEntity<?> getById(@PathVariable Long id) { return ResponseEntity.ok(courseService.getById(id)); }
    @GetMapping("/department/{deptId}") public ResponseEntity<?> getByDept(@PathVariable Long deptId) { return ResponseEntity.ok(courseService.getByDept(deptId)); }
    @GetMapping("/my") public ResponseEntity<?> getMyCourses(@AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        if (u.getRole().name().equals("FACULTY")) return ResponseEntity.ok(courseService.getByFaculty(u.getId()));
        return ResponseEntity.ok(courseService.getEnrollmentsByStudent(u.getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String,Object> req) {
        return ResponseEntity.ok(courseService.create(
            (String)req.get("name"), (String)req.get("code"), (String)req.get("description"),
            (Integer)req.get("credits"), (Integer)req.get("maxSeats"),
            Long.valueOf(req.get("departmentId").toString()),
            Long.valueOf(req.get("facultyId").toString())));
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<?> enroll(@PathVariable Long courseId, @AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        courseService.enroll(u.getId(), courseId);
        return ResponseEntity.ok(Map.of("message","Enrolled successfully"));
    }

    @GetMapping("/{courseId}/students")
    public ResponseEntity<?> getStudents(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getEnrollmentsByCourse(courseId));
    }
}
