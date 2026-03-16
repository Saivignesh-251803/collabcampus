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

@RestController @RequestMapping("/api/elections") @RequiredArgsConstructor
public class ElectionController {
    private final ElectionService electionService;
    private final UserService userService;

    @GetMapping("/public/all") public ResponseEntity<?> getPublic() { return ResponseEntity.ok(electionService.getAll()); }
    @GetMapping public ResponseEntity<?> getAll() { return ResponseEntity.ok(electionService.getAll()); }
    @GetMapping("/{id}") public ResponseEntity<?> getById(@PathVariable Long id) { return ResponseEntity.ok(electionService.getById(id)); }
    @GetMapping("/{id}/results") public ResponseEntity<?> results(@PathVariable Long id) { return ResponseEntity.ok(electionService.getResults(id)); }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String,Object> req, @AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(electionService.create((String)req.get("title"),(String)req.get("description"),
            LocalDateTime.parse((String)req.get("nominationStart")), LocalDateTime.parse((String)req.get("nominationEnd")),
            LocalDateTime.parse((String)req.get("votingStart")), LocalDateTime.parse((String)req.get("votingEnd")), u.getId()));
    }

    @PostMapping("/{electionId}/nominate")
    public ResponseEntity<?> nominate(@PathVariable Long electionId, @RequestBody Map<String,String> req, @AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(electionService.nominate(electionId, u.getId(), req.get("manifesto")));
    }

    @PostMapping("/{electionId}/vote")
    public ResponseEntity<?> vote(@PathVariable Long electionId, @RequestBody Map<String,Object> req, @AuthenticationPrincipal UserDetails ud) {
        User u = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(electionService.castVote(electionId, Long.valueOf(req.get("candidateId").toString()), u.getId()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String,String> req) {
        return ResponseEntity.ok(electionService.updateStatus(id, req.get("status")));
    }
}
