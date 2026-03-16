package com.collabcampus.controller;
import com.collabcampus.enums.Role;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/admin") @RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final DepartmentRepository deptRepo;
    private final AssignmentRepository assignRepo;
    private final GradeAppealRepository appealRepo;
    private final NoticeRepository noticeRepo;

    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        return ResponseEntity.ok(Map.of(
            "totalStudents", userRepo.findByRole(Role.STUDENT).size(),
            "totalFaculty", userRepo.findByRole(Role.FACULTY).size(),
            "totalCourses", courseRepo.count(),
            "totalDepartments", deptRepo.count(),
            "pendingAppeals", appealRepo.findByStatus("PENDING").size(),
            "pendingNotices", noticeRepo.findByApprovedFalse().size()
        ));
    }
}
