package com.collabcampus.service;
import com.collabcampus.entity.*;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service @RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceSessionRepository sessionRepo;
    private final AttendanceRecordRepository recordRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final EnrollmentRepository enrollRepo;

    public AttendanceSession createSession(Long courseId, Long facultyId) {
        Course course = courseRepo.findById(courseId).orElseThrow();
        User faculty = userRepo.findById(facultyId).orElseThrow();
        String qrToken = UUID.randomUUID().toString();
        return sessionRepo.save(AttendanceSession.builder()
            .course(course).faculty(faculty).qrToken(qrToken)
            .expiresAt(LocalDateTime.now().plusMinutes(5)).active(true).build());
    }

    public AttendanceRecord markAttendance(String qrToken, Long studentId) {
        AttendanceSession session = sessionRepo.findByQrTokenAndActiveTrue(qrToken)
            .orElseThrow(() -> new RuntimeException("Invalid or expired QR code"));
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setActive(false); sessionRepo.save(session);
            throw new RuntimeException("QR code has expired");
        }
        if (!enrollRepo.existsByStudentIdAndCourseId(studentId, session.getCourse().getId()))
            throw new RuntimeException("You are not enrolled in this course");
        if (recordRepo.existsByStudentIdAndSessionId(studentId, session.getId()))
            throw new RuntimeException("Attendance already marked");
        User student = userRepo.findById(studentId).orElseThrow();
        return recordRepo.save(AttendanceRecord.builder()
            .session(session).student(student).course(session.getCourse()).build());
    }

    public Map<String, Object> getReport(Long courseId, Long studentId) {
        List<AttendanceRecord> records = recordRepo.findByStudentIdAndCourseId(studentId, courseId);
        long totalSessions = sessionRepo.findByCourseId(courseId).size();
        long attended = records.size();
        double pct = totalSessions > 0 ? (attended * 100.0 / totalSessions) : 0;
        return Map.of("totalSessions", totalSessions, "attended", attended,
            "percentage", Math.round(pct), "records", records);
    }

    public List<AttendanceRecord> getSessionRecords(Long sessionId) {
        return recordRepo.findBySessionId(sessionId);
    }
}
