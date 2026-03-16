package com.collabcampus.controller;
import com.collabcampus.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/departments") @RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService deptService;

    @GetMapping public ResponseEntity<?> getAll() { return ResponseEntity.ok(deptService.getAll()); }
    @GetMapping("/{id}") public ResponseEntity<?> getById(@PathVariable Long id) { return ResponseEntity.ok(deptService.getById(id)); }

    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody Map<String,String> req) {
        return ResponseEntity.ok(deptService.create(req.get("name"),req.get("code"),req.get("description"),req.get("hodName")));
    }

    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String,String> req) {
        return ResponseEntity.ok(deptService.update(id,req.get("name"),req.get("description"),req.get("hodName")));
    }

    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        deptService.delete(id); return ResponseEntity.ok(Map.of("message","Deleted"));
    }
}
