package com.collabcampus.controller;
import com.collabcampus.service.*;
import com.collabcampus.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/notices") @RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;

    @GetMapping("/public/all") public ResponseEntity<?> getPublic() { return ResponseEntity.ok(noticeService.getAll()); }
    @GetMapping public ResponseEntity<?> getAll() { return ResponseEntity.ok(noticeService.getAll()); }
    @GetMapping("/department/{deptId}") public ResponseEntity<?> getByDept(@PathVariable Long deptId) { return ResponseEntity.ok(noticeService.getByDept(deptId)); }
    @GetMapping("/pending") public ResponseEntity<?> getPending() { return ResponseEntity.ok(noticeService.getPendingApproval()); }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String,Object> req, @AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        Long deptId = req.get("departmentId") != null ? Long.valueOf(req.get("departmentId").toString()) : null;
        return ResponseEntity.ok(noticeService.create((String)req.get("title"),(String)req.get("content"),
            (String)req.get("priority"), u.getId(), deptId));
    }

    @PutMapping("/{id}/approve") public ResponseEntity<?> approve(@PathVariable Long id) { return ResponseEntity.ok(noticeService.approve(id)); }
    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id) { noticeService.delete(id); return ResponseEntity.ok(Map.of("message","Deleted")); }
}
