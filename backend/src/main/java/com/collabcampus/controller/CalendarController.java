package com.collabcampus.controller;
import com.collabcampus.service.*;
import com.collabcampus.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController @RequestMapping("/api/events") @RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    private final UserService userService;

    @GetMapping("/public/all") public ResponseEntity<?> getPublic() { return ResponseEntity.ok(calendarService.getAll()); }
    @GetMapping public ResponseEntity<?> getAll() { return ResponseEntity.ok(calendarService.getAll()); }
    @GetMapping("/month/{year}/{month}") public ResponseEntity<?> getByMonth(@PathVariable int year, @PathVariable int month) { return ResponseEntity.ok(calendarService.getByMonth(year,month)); }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String,Object> req, @AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        LocalDate endDate = req.get("endDate") != null ? LocalDate.parse((String)req.get("endDate")) : null;
        return ResponseEntity.ok(calendarService.create((String)req.get("title"),(String)req.get("description"),
            (String)req.get("type"), LocalDate.parse((String)req.get("eventDate")), endDate, u.getId()));
    }

    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id) { calendarService.delete(id); return ResponseEntity.ok(Map.of("message","Deleted")); }
}
